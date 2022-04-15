package com.moppletop.dsb.jacksonutil;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class KeyValueDeserializerBase<T, A extends Annotation> extends StdDeserializer<T> {

    private final Map<String, Class<? extends T>> classes;

    public KeyValueDeserializerBase(Class<T> pojoClass, Class<A> annotationClass, Function<A, String> valueExtractor) {
        super(pojoClass);

        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);

        scanner.addIncludeFilter(new AnnotationTypeFilter(annotationClass));

        Set<BeanDefinition> beans = scanner.findCandidateComponents(annotationClass.getPackageName());
        this.classes = new HashMap<>(beans.size());

        for (BeanDefinition bean : beans) {
            try {
                Class<?> clazz = Class.forName(bean.getBeanClassName());

                if (!clazz.getSuperclass().equals(pojoClass)) {
                    continue;
                }

                A keyAnnotation = clazz.getDeclaredAnnotation(annotationClass);

                if (keyAnnotation == null) {
                    continue;
                }

                classes.put(valueExtractor.apply(keyAnnotation), (Class<? extends T>) clazz);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public T deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode root = parser.getCodec().readTree(parser);
        String key = root.get("key").asText();
        JsonNode values = root.get("values");
        Map<String, Object> parameters = new HashMap<>(values.size());

        values.fields().forEachRemaining(entry -> parameters.put(entry.getKey(), entry.getValue()));

        T object = construct(key, parameters);

        if (object == null) {
            throw new JsonMappingException(parser, key + " is not a registered " + handledType().getSimpleName());
        }

        return object;
    }

    private T construct(String key, Map<String, Object> parameters) {
        Class<? extends T> clazz = classes.get(key);

        if (clazz == null) {
            return null;
        }

        try {
            Constructor<? extends T> constructor = clazz.getConstructor(Map.class);

            constructor.setAccessible(true);

            return constructor.newInstance(parameters);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}

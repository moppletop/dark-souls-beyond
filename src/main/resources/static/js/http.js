const doVerb = async (url, method, init, parseJson) => {
    if (init.headers == null) {
        init.headers = new Headers();
    }

    addCsrfHeaders(init.headers);

    init.method = method;
    init.credentials = 'include';

    return await fetch(url, init).then(response => {
        if (errorHandle(response)) {
            return parseJson ? response.json() : response.text();
        }

        throw new Error('fetch response was not OK');
    }).catch(error => {
        console.warn(error);
    });
}

const doPost = (url, body) => {
    const headers = new Headers();
    headers.set('Content-Type', 'application/json');

    return doVerb(url, 'POST', { body: JSON.stringify(body), headers: headers }, true);
}

const doDelete = (url) => {
    return doVerb(url, 'DELETE', {}, true);
}

const doGet = (url, parseJson) => {
    return doVerb(url, 'GET', {}, parseJson);
}

function addCsrfHeaders(headers) {
    const header = document.querySelector('meta[name="_csrf_header"]').content;
    const token = document.querySelector('meta[name="_csrf"]').content;

    headers.set(header, token);
    return headers;
}

function errorHandle(response) {
    if (response.ok) {
        return true;
    }

    response.json().then(json => {
        let message = "An unknown error occurred";

        if (json.error != null) {
            message = json.error;
        } else if (json.message != null) {
            message = json.message;
        }

        alert(message);
    });

    return false;
}
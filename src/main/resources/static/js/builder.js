function submitBuilderCharacter(characterId) {
    const request = {name: document.getElementById('character-info-name').value};

    doPost('/api/character/' + characterId + '/name/', request).then(response => {
        window.location.href = '/ui/character/' + response.characterId + '/builder/origin';
    });
}

function submitBuilderOrigin(characterId, originId) {
    const request = {originId: originId};

    doPost('/api/character/' + characterId + '/origin/', request).then(response => {
        window.location.href = '/ui/character/' + response.characterId + '/builder/class';
    });
}

function submitBuilderClass(characterId, classId) {
    const request = {classId: classId};

    doPost('/api/character/' + characterId + '/class/', request).then(response => {
        window.location.href = '/ui/character/' + response.characterId + '/builder/class-features';
    });
}

function submitBuilderClassFeatures(characterId, id, max) {
    const parent = document.getElementById(id);
    const elements = parent.getElementsByTagName('input');
    let selected = 0;

    const selectedOptions = [];
    const featureName = parent.getAttribute('data-feature-name');

    for (let child of elements) {
        if (child.checked) {
            selected++;
            selectedOptions.push(child.getAttribute('data-option-name'));
        }
    }

    const valueMap = {};
    valueMap[featureName] = selectedOptions;
    const request = {valueMap: valueMap}
    doPost('/api/character/' + characterId + '/class-feature/', request).then(() => {
    });

    if (selected === max) {
        for (let child of elements) {
            if (!child.checked) {
                child.disabled = true;
            }
        }
    } else {
        for (let child of elements) {
            child.disabled = false;
        }
    }
}

function disableFeatures() {
    const fieldsets = document.getElementsByTagName('fieldset');

    for (let fieldset of fieldsets) {
        const elements = fieldset.getElementsByTagName('input');
        let selected = 0;

        for (let child of elements) {
            if (child.checked) {
                selected++;
            }
        }

        const max = parseInt(fieldset.getAttribute('data-feature-max'));

        if (selected === max) {
            for (let child of elements) {
                if (!child.checked) {
                    child.disabled = true;
                }
            }
        } else {
            for (let child of elements) {
                child.disabled = false;
            }
        }
    }
}

function goTo(characterId, page) {
    window.location.href = '/ui/character/' + characterId + '/builder/' + page
}

function setupCollapsibles() {
    const collapsibles = document.getElementsByClassName("collapsible");

    for (let collapsible of collapsibles) {
        collapsible.addEventListener("click", function () {
            this.classList.toggle("active");
            const content = this.nextElementSibling;
            if (content.style.display === "block") {
                content.style.display = "none";
            } else {
                content.style.display = "block";
            }
        });
    }
}


document.addEventListener('DOMContentLoaded', () => {
    setupCollapsibles();
    disableFeatures();
});

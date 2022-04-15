function spellSearch(characterId) {
    const params = []
    const parent = document.getElementById('families');
    const elements = parent.getElementsByTagName('input');

    for (let child of elements) {
        if (child.checked) {
            params.push('family=' + child.getAttribute('id'));
        }
    }

    const nameElement = document.getElementById('spell-name-input');

    if (nameElement.value !== '') {
        params.push('name=' + nameElement.value)
    }

    const url = '/ui/character/' + characterId + '/spell/search?' + params.join('&');

    doGet(url, false).then(content => {
        document.getElementById('table-content').innerHTML = content;
    });
}

function learnSpell(characterId, element) {
    const spell = element.getAttribute('data-spell');

    const request = {spell: spell};

    doPost('/api/character/' + characterId + '/spell/learn', request).then(json => {
        document.location.reload();
    });
}

function forgetSpell(characterId, element) {
    const spell = element.getAttribute('data-spell');

    const request = {spell: spell};

    doPost('/api/character/' + characterId + '/spell/forget', request).then(json => {
        document.location.reload();
    });
}

function attuneSpell(characterId, element) {
    const spell = element.getAttribute('data-spell');

    const request = {spell: spell};

    doPost('/api/character/' + characterId + '/spell/attune', request).then(json => {
        document.location.reload();
    });
}

function unattuneSpell(characterId, element) {
    const spell = element.getAttribute('data-spell');

    const request = {spell: spell};

    doPost('/api/character/' + characterId + '/spell/unattune', request).then(json => {
        document.location.reload();
    });
}

function moreSpellInfo(characterId, element) {
    const name = element.innerText;

    doGet('/ui/character/' + characterId + '/spell/search?name=' + name, false).then(content => {
        document.getElementById('table-content').innerHTML = content;
        window.location.hash = '';
        window.location.hash = 'table-content';
    });
}

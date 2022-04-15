function itemSearch(characterId) {
    const params = []
    const parent = document.getElementById('categories');
    const elements = parent.getElementsByTagName('input');

    for (let child of elements) {
        if (child.checked) {
            params.push('category=' + child.getAttribute('id'));
        }
    }

    const nameElement = document.getElementById('item-name-input');

    if (nameElement.value !== '') {
        params.push('name=' + nameElement.value)
    }

    const url = '/ui/character/' + characterId + '/item/search?' + params.join('&');

    doGet(url, false).then(content => {
        document.getElementById('table-content').innerHTML = content;
    });
}

function addItem(characterId, element) {
    const amountElement = element.getAttribute('data-amount');

    const slot = element.getAttribute('data-slot');
    const amount = amountElement == null ? 1 : document.getElementById(amountElement).value;
    const item = element.getAttribute('data-item');

    const request = {slot: slot, amount: amount, item: item};

    doPost('/api/character/' + characterId + '/inventory', request).then(json => {
        document.location.reload();
    });
}

function removeItem(characterId, itemInventoryId) {
    doDelete('/api/character/' + characterId + '/inventory/' + itemInventoryId).then(json => {
        document.location.reload();
    });
}

function setItem(characterId, itemInventoryId, element) {
    const amountElement = element.getAttribute('data-amount');

    const amount = amountElement == null ? 1 : document.getElementById(amountElement).value;

    const request = {amount: amount};

    doPost('/api/character/' + characterId + '/inventory/' + itemInventoryId + '/amount', request).then(json => {
        document.location.reload();
    });
}

function equipItem(characterId, itemInventoryId, element) {
    const slot = element.getAttribute('data-slot');
    const request = {slot: slot};

    doPost('/api/character/' + characterId + '/inventory/' + itemInventoryId + '/equip', request).then(json => {
        document.location.reload();
    });
}

function moreItemInfo(characterId, element) {
    const name = element.innerText;

    doGet('/ui/character/' + characterId + '/item/search?name=' + name, false).then(content => {
        document.getElementById('table-content').innerHTML = content;
        window.location.hash = '';
        window.location.hash = 'table-content';
    });
}
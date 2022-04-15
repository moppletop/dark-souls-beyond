function shortRest(characterId) {
    const rollElement = document.getElementById('position-roll-input');
    const rollValue = parseInt(rollElement.value);
    const diceElement = document.getElementById('position-dice-input');
    const diceValue = parseInt(diceElement.value);

    const request = {type: 'SHORT', position: rollValue, positionDiceSpent: diceValue};

    doPost('/api/character/' + characterId + '/rest', request).then(json => {
        document.location.reload();
    })
}

function longRest(characterId) {
    doPost('/api/character/' + characterId + '/rest', {type: 'LONG'}).then(json => {
        document.location.reload();
    })
}

function changeHealth(characterId, increase) {
    const element = document.getElementById('heal-damage-input');
    const value = parseInt(element.value);

    callPositionEndpoint(characterId, {position: increase ? value : -value, action: 'MOD'});
    element.value = '';
}

function startCombat(characterId) {
    const element = document.getElementById('combat-position-input');
    const value = parseInt(element.value);

    callPositionEndpoint(characterId, {position: value, action: 'START_COMBAT'});
    element.value = '';
}

function setPosition(characterId) {
    const element = document.getElementById('heal-damage-input');
    const value = parseInt(element.value);

    callPositionEndpoint(characterId, {position: value, action: 'SET'});
    element.value = '';
}

function stopCombat(characterId) {
    callPositionEndpoint(characterId, {action: 'STOP_COMBAT'});
}

function callPositionEndpoint(characterId, request) {
    doPost('/api/character/' + characterId + '/position', request).then(json => {
        document.getElementById('position-counter').innerText = json.position + '/' + json.maxPosition;

        if (json.position === 0) {
            $('#you-died').modal('toggle');
            return;
        }

        const bloodied = document.getElementById('is-bloodied').innerText === 'true';

        if (bloodied !== json.bloodied) {
            document.location.reload();
        }
    })
}

function addSouls(characterId, increase) {
    const element = document.getElementById('souls-input');
    const value = parseInt(element.value);

    const request = {souls: (increase ? value : -value)};

    element.value = '';

    doPost('/api/character/' + characterId + '/souls/on-person', request).then(json => {
        document.getElementById('souls-counter').innerText = json.souls;
    })
}

function die(characterId) {
    doPost('/api/character/' + characterId + '/die', {}).then(json => {
        document.location.reload();
    })
}

function setUses(characterId, element, increase) {
    const key = element.getAttribute('data-key');
    const counter = element.getAttribute('data-counter');
    const counterElement = document.getElementById(counter);
    const split = counterElement.innerText.split("/");

    let currentUses = parseInt(split[0]);
    const maxUses = parseInt(split[1]);

    if (increase && currentUses !== maxUses) {
        currentUses++;
    } else if (!increase && currentUses !== 0) {
        currentUses--;
    }

    const request = {key: key, uses: (maxUses - currentUses)};

    doPost('/api/character/' + characterId + '/uses', request).then(json => {
        counterElement.innerHTML = currentUses + '/' + maxUses;
    });
}


function setSpellCasts(characterId, element, increase) {
    const spell = element.getAttribute('data-spell');
    const counter = element.getAttribute('data-counter');
    const counterElement = document.getElementById(counter);
    const split = counterElement.innerText.split("/");

    let currentCasts = parseInt(split[0]);
    const maxCasts = parseInt(split[1]);

    if (increase && currentCasts !== maxCasts) {
        currentCasts++;
    } else if (!increase && currentCasts !== 0) {
        currentCasts--;
    }

    const request = {spell: spell, casts: (maxCasts - currentCasts)};

    doPost('/api/character/' + characterId + '/spell/casts', request).then(json => {
        counterElement.innerHTML = currentCasts + '/' + maxCasts;
    });
}

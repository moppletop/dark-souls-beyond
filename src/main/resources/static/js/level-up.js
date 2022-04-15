function bankSouls(characterId) {
    const element = document.getElementById('banked-souls-input');
    const value = parseInt(element.value);

    const request = {souls: value};

    doPost('/api/character/' + characterId + '/souls/bank', request).then(json => {
        document.location.reload();
    })
}

function levelUp(characterId) {
    doPost('/api/character/' + characterId + '/level-up', {}).then(json => {
        if (json.newFeatures) {
            window.location.href = '/ui/character/' + characterId + '/builder/class-features';
        } else {
            window.location.href = '/ui/character/' + characterId;
        }
    })
}

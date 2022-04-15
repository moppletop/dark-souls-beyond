function createCharacter(userId) {
    const request = {userId: userId};

    doPost('/api/character/', request).then(response => {
        window.location.href = '/ui/character/' + response.characterId + '/builder/character';
    });
}

function deleteCharacter(characterId) {
    doDelete('/api/character/' + characterId).then(response => {
        document.location.reload();
    });
}

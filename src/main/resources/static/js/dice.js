const rollDice = (expression) => {
    return doGet('/api/dice/roll?expression=' + encodeURIComponent(expression), true);
}

function rollDiceThroughModal() {
    const expression = document.getElementById('dice-roll-input').value;

    rollDice(expression).then(result => {
        document.getElementById('dice-roll-total').innerHTML = result.total;
        document.getElementById('dice-roll-rolls').innerHTML = result.rolls;
    })
}
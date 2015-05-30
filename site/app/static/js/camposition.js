camposition_url = 'http://143.107.235.44:5000/camposition/'

$(document).ready(function() {
    $('#btn-up').click(moveup);
    $('#btn-left').click(moveleft);
    $('#btn-right').click(moveright);
    $('#btn-down').click(movedown);
});

function moveup() {
    $.get(camposition_url, {'move': 2, 'degree': 5})
}

function moveleft() {
    $.get(camposition_url, {'move': 6, 'degree': 5})
}

function moveright() {
    $.get(camposition_url, {'move': 4, 'degree': 5})
}

function movedown() {
    $.get(camposition_url, {'move': 0, 'degree': 5})
}


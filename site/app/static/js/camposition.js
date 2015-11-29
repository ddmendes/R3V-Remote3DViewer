camposition_url = 'http://192.168.1.107:5000/camposition/cam_step/' // 'http://143.107.235.44:5000/camposition/cam_step/'

$(document).ready(function() {
    $('#btn-up').click(moveup);
    $('#btn-left').click(moveleft);
    $('#btn-right').click(moveright);
    $('#btn-down').click(movedown);
});

function moveup() {
    $.get(camposition_url, {'move': 2})
}

function moveleft() {
    $.get(camposition_url, {'move': 6})
}

function moveright() {
    $.get(camposition_url, {'move': 4})
}

function movedown() {
    $.get(camposition_url, {'move': 0})
}


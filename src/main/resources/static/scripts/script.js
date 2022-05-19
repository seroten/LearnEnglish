let show = false;

function showWord() {
    document.getElementById('hidden_word').style.opacity = "0.6";
    document.getElementById('showButtontext').innerHTML = "Next";
    document.getElementById('bottomButtontext').innerHTML = "Repeat the word";
    if (show) {
        document.getElementById('button_1')
            .setAttribute('type', 'submit');
        document.getElementById('button_2')
            .setAttribute('type', 'submit');

    }
    show = !show;
}


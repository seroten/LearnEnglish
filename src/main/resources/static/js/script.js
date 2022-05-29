let show = false;

function showWord() {
    document.getElementById('hidden_word').style.opacity = "0.6";
    document.getElementById('next').innerHTML = "Next";
    // document.getElementById('repeat_button').style.display = "flex";

    if (show) {
        document.getElementById('button_1')
            .setAttribute('type', 'submit');
        document.getElementById('button_2')
            .setAttribute('type', 'submit');
        document.getElementById('button_3')
            .setAttribute('type', 'submit');

    }
    show = !show;
}

function previous(wordNumber) {
    document.getElementById('wordsNumber').setAttribute('value', (wordNumber - 1).toString())
    document.getElementById('button_1')
        .setAttribute('type', 'submit');

}

function unlearned(wordNumber) {
    document.getElementById('wordsNumber').setAttribute('value', (wordNumber).toString())
    document.getElementById('button_3')
        .setAttribute('type', 'submit');
}

function speak(text) {
    const message = new SpeechSynthesisUtterance();
    message.lang = "en-EN";
    message.text = text;
    window.speechSynthesis.speak(message)
}


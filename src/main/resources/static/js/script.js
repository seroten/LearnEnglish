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

let guessed = false
let wrongChoice = false;

function paintButton(word, buttonWord) {
    if (guessed != true) {
        if (word === buttonWord) {
            document.getElementById(buttonWord).style.background = "#2e9b54";
            document.getElementById('next_button_div').style.display = "flex";
            guessed = true;
        } else {
            document.getElementById(buttonWord).style.background = "#cd0c0c";
            document.getElementById('wrongChoice').setAttribute('value', 'wrongChoice');
        }
    }
}

let wordCursor = 0;

function choiceLetter(word, letter, id) {
    if(word.charAt(wordCursor) === letter) {
        document.getElementById(id).style.visibility = "hidden";
        document.getElementById(letter + wordCursor).style.display = "block";
        wordCursor++;
        if(word.length === wordCursor) {
            document.getElementById('next_button_div').style.display = "flex";
        }
    } else {
        document.getElementById('wrongChoice').setAttribute('value', 'wrongChoice');
        document.getElementById(id).style.background = "#cd0c0c";
        setTimeout(() => document.getElementById(id).style.background = "#ffffff", 1000);
    }
}
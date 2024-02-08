let show = false;

function showWord() {
    document.getElementById('hidden_word').style.opacity = "0.6";
    document.getElementById('next').innerHTML = "Next";

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
    document.getElementById('wordsNumber').setAttribute('value', (wordNumber - 1).toString());
    document.getElementById('button_1').setAttribute('type', 'submit');

}

function unlearned(wordNumber) {
    document.getElementById('wordsNumber').setAttribute('value', (wordNumber).toString());
    document.getElementById('button_3').setAttribute('type', 'submit');
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
            document.getElementById('word_guessed').style.visibility = "visible";
            if (document.getElementById('ear_word_guessed') !== null) {
                document.getElementById('ear_word_guessed').style.visibility = "visible";
            }
            guessed = true;
            speak(word);
        } else {
            document.getElementById(buttonWord).style.background = "#cd0c0c";
            document.getElementById('choiceStatus').setAttribute('value', 'wrongChoice');
        }
    }
}

let wordCursor = 0;

function choiceLetter(word, letter, id) {
    console.log(word);
    console.log(word.charAt(wordCursor));
    console.log(letter);
    console.log(id);
    if (word.charAt(wordCursor) === letter) {
        document.getElementById(id).style.visibility = "hidden";
        document.getElementById(letter + wordCursor).style.display = "block";
        wordCursor++;
        if (word.length === wordCursor) {
            document.getElementById('next_button_div').style.display = "flex";
            document.getElementById('word_guessed').style.visibility = "visible";
            speak(word);
            document.getElementById('choiceStatus').setAttribute('value', 'rightChoice');
            if (document.getElementById('choiceStatus').getAttribute('value') !== 'wrongChoice') {
                document.getElementById('choiceStatus').setAttribute('value', 'rightChoice');
            }
        }
    }
    // if (word.charAt(wordCursor + 1) === letter && (word.charAt(wordCursor) === ` `)) {
    //     document.getElementById(id).style.visibility = "hidden";//TODO
      else {
        document.getElementById('choiceStatus').setAttribute('value', 'wrongChoice');
        document.getElementById(id).style.background = "#cd0c0c";
        setTimeout(() => document.getElementById(id).style.background = "#ffffff", 500);
    }
}

let checked = false;

function checkInput(word) {
    if (!checked) {
        document.getElementById('next_button_div').style.display = "flex";
        document.getElementById('word_guessed').style.visibility = "visible";
        speak(word);
        if (document.getElementById('input_text').value.toLowerCase() === word.toLowerCase()) {
            document.getElementById('checkedInput').setAttribute('value', 'rightChoice');
        } else {
            document.getElementById('checkedInput').setAttribute('value', 'wrongChoice');
            document.getElementById('input_text').style.borderColor = "red";
        }
        document.getElementById('input_text').setAttribute('disabled', 'disabled');
        document.getElementById('input_text').setAttribute('placeholder', '');
        checked = true;
    }
}
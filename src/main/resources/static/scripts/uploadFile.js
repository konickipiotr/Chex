function chooseImage(){
    document.getElementById("picture").click();
}

function sub() {    
    document.pictureForm.submit();
  }

  

function returnFileSize(number) {
    if(number < 1024) {
        return number + 'bytes';
    } else if(number >= 1024 && number < 1048576) {
        return (number/1024).toFixed(1) + 'KB';
    } else if(number >= 1048576) {
        return (number/1048576).toFixed(1) + 'MB';
    }
}

// document.getElementById("picture").onchange = function(e){
//     const name = this.value.split(/(\\|\/)/g).pop();
//     const size = returnFileSize(this.files[0].size);
//     const output = name + " - " + size;

//     document.getElementById("fileinfo").innerHTML = output;
// }


function displayFileInfo(file){
    const name = file.value.split(/(\\|\/)/g).pop();
    const size = returnFileSize(file.files[0].size);
    const output = name + " - " + size;

    document.getElementById("fileinfo").innerHTML = output;
}

// document.getElementById("sameAsFirst").addEventListener('change', (event) => {
//     let secondField = document.getElementById("namePl");
//     if (event.currentTarget.checked) {
//         secondField.value = "";
//         secondField.disabled = true;
//         secondField.style.background = "#c9c9c9";
//     } else {
//         secondField.disabled = false;
//         secondField.style.background = "white";
//     }
// })
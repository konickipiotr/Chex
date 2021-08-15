
function unlockFields(id){
    const plId = "pl" + id;
    const engId = "eng" + id;

    const editBtnId = "editBtn" + id;
    const saveBtnId = "saveBtn" + id;

    const plField = document.getElementById(plId);
    const engField = document.getElementById(engId);
    
    const editBtn = document.getElementById(editBtnId);
    const saveBtn = document.getElementById(saveBtnId);
    
    if(plField.disabled == true){
        plField.disabled = false;
        engField.disabled = false;
        editBtn.hidden = true;
        saveBtn.hidden = false;
    }else{
        plField.disabled = true;
        engField.disabled = true;
        editBtn.hidden = false;
        saveBtn.hidden = true;
    }
}


const profileName = document.getElementById('profileName');

profileName.addEventListener('click', function() {
    const newName = prompt("새로운 닉네임을 입력해주세요.", profileName.innerText);
        if (newName !== null && newName.trim() !== "") {
        profileName.innerText = newName;
    }
});
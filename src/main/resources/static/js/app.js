console.log("hello world :)")
const btn = document.getElementById('showAllBtn')
let btnPressed = false;

btn.addEventListener('click', function (e) {
    e.preventDefault();
    if(!btnPressed){
        let toHide = btn.parentElement.parentElement.children
        Array.from(toHide).forEach((e, i) => { if(i !== 0) e.style.display = "none"})
        btnPressed = true;
        btn.textContent = "Show all"
        console.log('btn pressed');
    } else {
        let toHide = btn.parentElement.parentElement.children
        Array.from(toHide).forEach((e, i) => { if(i !== 0) e.style.display = ''})
        btnPressed = false;
        btn.textContent = "Hide all"
        console.log('btn pressed');
    }
})
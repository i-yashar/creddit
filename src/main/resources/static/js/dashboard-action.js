let upVoteBtns = Array.from(document.getElementsByClassName('upVoteBtn'));
let downVoteBtns = Array.from(document.getElementsByClassName('downVoteBtn'));

upVoteBtns.forEach(b => {
    b.addEventListener('click', upVoteHandler)
})

downVoteBtns.forEach(b => {
    b.addEventListener('click', downVoteHandler)
})

async function upVoteHandler(event) {
    event.preventDefault();
    const postId = event.target.parentElement.parentElement.parentElement.parentElement.getAttribute('id').substring(6);

    const response = await fetch("http://localhost:8080/api/posts/upvote/" + postId, {
            method: 'GET'
        }
    );

    const data = await response.json();

    event.target.childNodes[1].textContent = data.upVoteCount;
    event.target.className = event.target.className.includes('success') ? 'upVoteBtn btn btn-primary' : 'upVoteBtn btn btn-success';
    event.target.parentElement.parentElement.childNodes[3].firstElementChild.className = 'downVoteBtn btn btn-primary';
}

async function downVoteHandler(event) {
    event.preventDefault();
    const postId = event.target.parentElement.parentElement.parentElement.parentElement.getAttribute('id').substring(6);

    const response = await fetch("http://localhost:8080/api/posts/downvote/" + postId, {
            method: 'GET'
        }
    );

    const data = await response.json();

    event.target.parentElement.previousElementSibling.firstElementChild.childNodes[1].textContent = data.upVoteCount;
    event.target.className = event.target.className.includes('primary') ? 'downVoteBtn btn btn-danger' : 'downVoteBtn btn btn-primary';
    event.target.parentElement.parentElement.childNodes[1].firstElementChild.className = 'upVoteBtn btn btn-primary';
}
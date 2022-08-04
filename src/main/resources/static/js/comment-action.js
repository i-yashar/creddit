let commentUpVoteBtns = Array.from(document.getElementsByClassName('upVoteCommentBtn'));
let commentDownVoteBtns = Array.from(document.getElementsByClassName('downVoteCommentBtn'));

commentUpVoteBtns.forEach(b => {
    b.addEventListener('click', commentUpVoteHandler)
})

commentDownVoteBtns.forEach(b => {
    b.addEventListener('click', commentDownVoteHandler)
})

async function commentUpVoteHandler(event) {
    event.preventDefault();
    const commentId = event.target.parentElement.parentElement.parentElement.parentElement.getAttribute('id').substring(9);

    const response = await fetch("http://localhost:8080/api/posts/comments/upvote/" + commentId, {
            method: 'GET'
        }
    );

    const data = await response.json();

    console.log(data);

    event.target.childNodes[1].textContent = data.upVoteCount;
    event.target.className = event.target.className.includes('success') ? 'upVoteCommentBtn btn btn-primary btn-sm' : 'upVoteCommentBtn btn btn-success btn-sm';
    event.target.parentElement.parentElement.childNodes[3].firstElementChild.className = 'downVoteCommentBtn btn btn-primary btn-sm';
}

async function commentDownVoteHandler(event) {
    event.preventDefault();
    const commentId = event.target.parentElement.parentElement.parentElement.parentElement.getAttribute('id').substring(9);

    const response = await fetch("http://localhost:8080/api/posts/comments/downvote/" + commentId, {
            method: 'GET'
        }
    );

    const data = await response.json();

    console.log(data);

    event.target.parentElement.previousElementSibling.firstElementChild.childNodes[1].textContent = data.upVoteCount;
    event.target.className = event.target.className.includes('primary') ? 'downVoteCommentBtn btn btn-danger btn-sm' : 'downVoteCommentBtn btn btn-primary btn-sm';
    event.target.parentElement.parentElement.childNodes[1].firstElementChild.className = 'upVoteCommentBtn btn btn-primary btn-sm';
}
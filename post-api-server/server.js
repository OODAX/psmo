const express = require('express');
const app = express();
const port = 3000;

// Mock data
const posts = [
    {
        userId: 1,
        id: 1,
        title: "Post Title 1",
        body: "This is the body of post 1"
    },
    {
        userId: 1,
        id: 2,
        title: "Post Title 2",
        body: "This is the body of post 2"
    },
    {
        userId: 2,
        id: 3,
        title: "Post Title 3",
        body: "This is the body of post 3"
    }
];

// Define the /posts endpoint
app.get('/posts', (req, res) => {
    res.json(posts);
});

// Start the server
app.listen(port, () => {
    console.log(`Server running at http://localhost:${port}`);
});

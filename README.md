# TicTacToeServer

TicTacToe backend is built with Ktor framework and deployed on the Heroku.

Currently this API is deployed on _`https://tic-tac-toe-vaibhav.herokuapp.com`_. You can try it 😃.

## Features 👓

- Easy structure
- Online Game mode using WebSockets
- Automatic and easy deployment to Heroku

## Development Setup 🖥

You will require latest stable version of JetBrains IntelliJ Idea to build and run the server application.

- Import this project in IntelliJ Idea
- Build the project.
- Set environment variables for the `:application:run` configuration as following

```
PORT=8080
```

# REST API Routes

### Create Room

```http
POST https://tic-tac-toe-vaibhav.herokuapp.com/v1/room/create
Content-Type: application/json

{
    "roomName": "ROOM_1"
}

```

### Join Room

```http
GET https://tic-tac-toe-vaibhav.herokuapp.com/v1/room/join
Content-Type: application/json
```

### Get all Rooms

```http
GET https://tic-tac-toe-vaibhav.herokuapp.com/v1/room/search
Content-Type: application/json
```

# Note

### Want to learn how to develop this Server application, check out my Medium article where I've explained everything.
https://medium.com/@vgoyal_1/creating-an-online-tictactoe-game-using-websocket-ktor-part1-4a45167c9096
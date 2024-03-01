# Server JSON Request Handler

## Overview
This project extends the functionality of a simple server application to manage a dynamic list of strings. It defines a protocol for handling various actions such as adding, clearing, finding, displaying, deleting, and prepending strings within a list, through JSON formatted requests and responses. 

## Protocol Specification

### Sending Requests
Clients can interact with the server using structured JSON requests:
```json
{
  "selected": "<action_code>",
  "data": "<payload>"
}
```
- **action_code**: An integer representing the desired action (1 for add, 2 for clear, etc.).
- **payload**: The content varies based on the action, ranging from strings to be added to the list, to integers indicating an index for deletion or prepending.

### Actions
- **Add (1)**: Adds a provided string to the list. 
- **Clear (2)**: Clears the entire list. No payload required.
- **Find (3)**: Searches for a string and returns its index or -1 if not found.
- **Display (4)**: Displays the entire list. No payload required.
- **Delete (5)**: Deletes the entry at the specified index.
- **Prepend (6)**: Prepends a string to an existing entry at the given index.
- **Quit (0)**: Closes the connection. No payload required.

### Handling Responses
The server responds with a JSON object indicating success or failure:
- **Success**: `{"ok": true, "type": "<action_string>", "data": "<response_data>"}`
- **Failure**: `{"ok": false, "message": "<error_message>"}`

### Running the Application
#### Using Terminal:
- **Server**: Execute `gradle runServer -Pport=9099 -q --console=plain` to start.
- **Client**: Use `gradle runClient -Phost=localhost -Pport=9099 -q --console=plain` to connect.
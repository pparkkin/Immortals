Network Protocol
================

All messages start with a one-byte command, followed by the message content, which depends on the command.

Messages
--------

Join
~~~~

A client should send a Join command immediately after connecting to a server to announce himself.

Command: 0x4a
Content: Name of the player as string (characted encoding undefined at this point)

Welcome
~~~~~~~

A server will respond to a Join command with a Welcome command to introduce itself.

Command: 0x57
Content: Name of the game as string (characted encoding undefined at this point)

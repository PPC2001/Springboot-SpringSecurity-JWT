Key features of Spring Security used in project:
----------------

Built-in Authentication Mechanisms: Supports various authentication methods like HTTP Basic, Form-based authentication, Digest, and OAuth2.

Custom Authentication Providers: Allows creating custom authentication logic (e.g., DaoAuthenticationProvider, LDAP, or custom databases).

Third-party Authentication: Integrates with third-party services such as OAuth2, OpenID Connect, JWT, and SAML for Single Sign-On (SSO).

Password Encoding: Provides out-of-the-box password encoding using secure algorithms like BCrypt, SCrypt, and Argon2.


------------
JWT (JSON Web Token) is a compact, URL-safe way to represent claims securely between two parties (usually a client and a server).
JWT consists of three parts: a Header, Payload, and Signature.

Login:

1.Client sends credentials to the server.
2.Server validates credentials and generates a JWT.
3.Server sends the JWT back to the client.

Accessing a Resource:

1.Client sends the JWT in the Authorization header.
2.Server validates the JWT (checks signature, expiration, etc.).
3.Server processes the request if the JWT is valid.

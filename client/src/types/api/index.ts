export type AuthResponseType = {
    firstName: string,
    lastName: string,
    email: string
    profileImg?: string,
    roles: string[],
    accessToken: string
}

export type AuthRequestType = {
    email: string,
    password: string
}

export type RegisterRequestType = {
    firstName: string,
    lastName: string,
    email: string,
    password: string,
    verifyPassword: string
}
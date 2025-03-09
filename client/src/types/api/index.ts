import { ArticlePreviewType } from "../model"

export type AuthResponseType = {
    id: string,
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

export type ImageUploadReponseType = {
    fileId: string,
    name: string,
    size: number,
    filePath: string,
    url: string,
    height: number,
    width: number,
    thumbnailUrl: string
}

export type ArticlesPageResponseType = {
    articles: ArticlePreviewType[],
    hasMorePages: boolean
}
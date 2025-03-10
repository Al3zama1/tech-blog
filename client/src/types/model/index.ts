import { Delta } from "quill"

export type UserType = {
    id: string,
    firstName: string,
    lastName: string,
    email: string,
    profileImg?: string,
    roles: string[]
}

export type ImageType = {
    filePath: string,
    thumbnailUrl: string,
    fileId: string,
    url: string,
    name: string,
}

export type DraftType = {
    title: string,
    content: string
    description: string,
    coverImg: ImageType | null,
    category: string
}

export type ArticleType = {
    title: string,
    content: string,
    description: string,
    category: string,
    author: AuthorType,
    isFeatured: boolean
    createdAt: Date,
    coverImg: string | null
}

export type ArticlePreviewType = {
    title: string,
    author: AuthorType,
    category: string,
    description: string,
    createdAt: string,
    coverImg: string,
    slug: string
  }

  export type AuthorType = {
    id: string,
    firstName: string,
    lastName: string,
    profileImg?: string,
    bio?: string,
    linkedInUrl?: string
    gitHubUrl?: string
  }
export default function formatCategory(category: string) : string {
    return category.replace(/\b\w/g, (char) => char.toUpperCase());

}
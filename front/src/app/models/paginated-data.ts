export interface PaginatedData<T> {
    content: T[];
    totalElements: number;
    size: number;
    number: number;
}  
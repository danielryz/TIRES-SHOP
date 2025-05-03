export interface CartItem {
    id: number;
    productId: number;
    productName: string;
    quantity: number;
    pricePerItem: number;
    totalPrice: number;
    imageUrl?: string;
}

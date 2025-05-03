
export type ProductType = 'TIRE' | 'ACCESSORY' | 'RIM' | 'ALL';

export interface Tire {
    id: number;
    name: string;
    price: number;
    size: string;
    season: string;
    stock: number;
    description: string;
    productType: ProductType;
    imageUrls?: string[];
}


export type ProductType = 'TIRE' | 'ACCESSORY' | 'RIM' | 'ALL';

export interface Rim {
    id: number;
    name: string;
    price: number;
    stock: number;
    description: string;
    productType: ProductType;
    size: string;
    material: string;
    boltPattern: string;
    imageUrls?: string[];
}


export type AddressType = 'BILLING' | 'SHIPPING' | 'RESIDENTIAL';

export interface Address {
    id: number;
    street: string;
    houseNumber: string;
    apartmentNumber: string;
    postalCode: string;
    city: string;
    type: AddressType;
}
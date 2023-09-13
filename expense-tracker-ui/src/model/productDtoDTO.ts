/**
 * Expense Tracker API Contract
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: 0.0.5-SNAPSHOT
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
import { CategoryDto } from './categoryDtoDTO';


export interface ProductDto { 
    id: string;
    category: CategoryDto;
    name: string;
    amount: number;
    price: number;
    description?: string;
}


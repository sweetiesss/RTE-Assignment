// src/api/categories.ts
import { fetchWithAuth } from "./config";

export const categories = {
  getAll: ({
    page,
    size,
    sort,
    search = "",
  }: {
    page: number;
    size: number;
    sort: string;
    search?: string;
  }) =>
    fetchWithAuth(
      `/categories?page=${page}&size=${size}&search=${search}&sort=${sort}`
    ),

  getByProductId: (productId: string) =>
    fetchWithAuth(`/product-categories/${productId}`),

  create: (categoryData: { name: string; description: string }) =>
    fetchWithAuth("/admin/categories", {
      method: "POST",
      body: JSON.stringify(categoryData),
    }),

  update: (id: number, categoryData: { name: string; description: string }) =>
    fetchWithAuth(`/admin/categories/${id}`, {
      method: "PUT",
      body: JSON.stringify(categoryData),
    }),

  delete: (id: number) =>
    fetchWithAuth(`/admin/categories/${id}`, {
      method: "DELETE",
    }),
};

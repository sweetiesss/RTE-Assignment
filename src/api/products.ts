// src/api/products.ts
import { fetchWithAuth, API_BASE_URL, getAuthToken } from "./config";

export const products = {
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
      `/products?page=${page}&size=${size}&sort=${sort}&search=${encodeURIComponent(
        search
      )}`
    ),

  getById: (id: string) => fetchWithAuth(`/products/${id}`),

  create: (productData: {
    name: string;
    description: string;
    price: number;
    featured: boolean;
  }) =>
    fetchWithAuth("/admin/products", {
      method: "POST",
      body: JSON.stringify(productData),
    }),

  updateAdmin: (
    id: string,
    productData: {
      name: string;
      description: string;
      price: number;
      featured: boolean;
    }
  ) =>
    fetchWithAuth(`/admin/products/${id}`, {
      method: "PUT",
      body: JSON.stringify(productData),
    }),

  deleteAdmin: (id: string) =>
    fetchWithAuth(`/admin/products/${id}`, {
      method: "DELETE",
    }),

  getFeatured: ({
    page,
    size,
    sort,
  }: {
    page: number;
    size: number;
    sort: string;
  }) =>
    fetchWithAuth(`/products/featured?page=${page}&size=${size}&sort=${sort}`),

  getByCategory: (
    categoryId: number,
    {
      page,
      size,
      sort,
      search,
    }: { page: number; size: number; sort: string; search?: string }
  ) =>
    fetchWithAuth(
      `/categories/${categoryId}/products?page=${page}&pageSize=${size}&sort=${sort}&search=${
        search || ""
      }`
    ),

  uploadImage: (productId: string, file: File) => {
    const formData = new FormData();
    formData.append("file", file);

    return fetch(`${API_BASE_URL}/admin/products/${productId}/image`, {
      method: "POST",
      headers: {
        Authorization: `Bearer ${getAuthToken()}`,
      },
      body: formData,
    });
  },

  assignCategoriesToProduct: (productId: number, categoryIds: number[]) => {
    return fetchWithAuth(`/admin/product-categories`, {
      method: "POST",
      body: JSON.stringify({ productId, categoryId: categoryIds }),
    });
  },
};

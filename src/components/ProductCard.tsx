import React, { useState } from "react";
import { Link } from "react-router-dom";
import { Heart, ShoppingBag, Edit, Trash2 } from "react-feather";
import { api } from "../api";
import { useAuth } from "../context/AuthContext";
import StarRating from "./StarRating";

type Product = {
  id: string;
  name: string;
  description: string;
  image?: string;
  averageRating?: number;
  price?: number;
  featured: boolean;
};

type ProductCardProps = {
  product: Product;
  onUpdate: () => void;
};

const ProductCard: React.FC<ProductCardProps> = ({ product, onUpdate }) => {
  const { user } = useAuth();
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [showDeleteConfirmation, setShowDeleteConfirmation] = useState(false);
  const [updatedProduct, setUpdatedProduct] = useState({
    name: product.name,
    description: product.description,
    price: product.price || 0,
    featured: product.featured,
  });

  const handleUpdate = async () => {
    try {
      await api.products.updateAdmin(product.id, updatedProduct);
      setIsModalOpen(false); // Close the modal
      onUpdate(); // Refresh the product list
    } catch (error) {
      console.error("Error updating product:", error);
      alert("Failed to update product.");
    }
  };

  const handleDelete = async () => {
    try {
      await api.products.deleteAdmin(product.id);
      setShowDeleteConfirmation(false);
      onUpdate(); // Notify parent to refresh the product list
    } catch (error) {
      console.error("Error deleting product:", error);
      alert("Failed to delete product.");
    }
  };

  return (
    <div className="bg-white rounded-lg overflow-hidden shadow-md hover:shadow-lg transition-shadow">
      <div className="relative">
        <img
          src={product.image}
          alt={product.name}
          className="w-full h-48 object-contain bg-gray-100"
        />
        <button
          className={`absolute top-2 right-2 p-1.5 rounded-full shadow-md ${
            product.featured
              ? "bg-red-100 hover:bg-red-200"
              : "bg-white hover:bg-gray-100"
          }`}
          title={product.featured ? "Featured Product" : "Mark as Featured"}
        >
          <Heart
            size={18}
            className={product.featured ? "text-red-600" : "text-gray-600"}
          />
        </button>
      </div>
      <div className="p-4">
        <h3 className="font-semibold text-lg mb-1 line-clamp-1">
          {product.name}
        </h3>
        <div className="flex items-center mb-2">
          <StarRating rating={product.averageRating || 0} />
          <span className="ml-1 text-xs text-gray-600">
            ({product.averageRating ? product.averageRating.toFixed(1) : "0"})
          </span>
        </div>
        <p className="text-gray-600 text-sm mb-3 line-clamp-1">
          {product.description}
        </p>
        <div className="flex items-center justify-between">
          <span className="font-bold text-lg">${product.price}</span>
          <div className="flex items-center gap-2">
            {user?.role === "ADMIN" && (
              <>
                {/* Update Button */}
                <button
                  onClick={() => setIsModalOpen(true)} // Open the modal
                  className="p-2 text-blue-600 bg-blue-100 rounded-md hover:bg-blue-200 transition-all"
                  title="Update"
                >
                  <Edit className="w-5 h-5" />
                </button>

                {/* Delete Button */}
                <button
                  onClick={() => setShowDeleteConfirmation(true)} // Show confirmation modal
                  className="p-2 text-red-600 bg-red-100 rounded-md hover:bg-red-200 transition-all"
                  title="Delete"
                >
                  <Trash2 className="w-5 h-5" />
                </button>

                {/* Delete Confirmation Modal */}
                {showDeleteConfirmation && (
                  <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
                    <div className="bg-white p-6 rounded-lg shadow-lg w-full max-w-md">
                      <h2 className="text-xl font-bold mb-4">
                        Confirm Deletion
                      </h2>
                      <p className="text-gray-600 mb-6">
                        Are you sure you want to delete{" "}
                        <strong>{product.name}</strong>? This action cannot be
                        undone.
                      </p>
                      <div className="flex justify-end gap-4">
                        <button
                          onClick={() => setShowDeleteConfirmation(false)} // Close the modal
                          className="px-4 py-2 bg-gray-300 text-gray-800 rounded-md hover:bg-gray-400"
                        >
                          Cancel
                        </button>
                        <button
                          onClick={handleDelete} // Call the delete handler
                          className="px-4 py-2 bg-red-600 text-white rounded-md hover:bg-red-700"
                        >
                          Delete
                        </button>
                      </div>
                    </div>
                  </div>
                )}

                {/* View Button */}
                <Link
                  to={`/product/${product.id}`}
                  className="bg-indigo-600 text-white p-2 rounded-md hover:bg-indigo-700 transition-colors"
                  title="View Product"
                >
                  <ShoppingBag size={18} />
                </Link>
              </>
            )}
          </div>
        </div>
      </div>

      {/* Modal for Updating Product */}
      {isModalOpen && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white p-8 rounded-lg shadow-lg w-full max-w-4xl">
            <h2 className="text-2xl font-bold mb-6">Update Product</h2>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Product Name
                </label>
                <input
                  type="text"
                  placeholder="Product Name"
                  value={updatedProduct.name}
                  onChange={(e) =>
                    setUpdatedProduct({
                      ...updatedProduct,
                      name: e.target.value,
                    })
                  }
                  className="w-full px-4 py-2 border rounded-md"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Price
                </label>
                <input
                  type="number"
                  placeholder="Price"
                  value={updatedProduct.price}
                  onChange={(e) =>
                    setUpdatedProduct({
                      ...updatedProduct,
                      price: parseFloat(e.target.value),
                    })
                  }
                  className="w-full px-4 py-2 border rounded-md"
                />
              </div>
              <div className="flex items-center gap-2 col-span-2">
                <input
                  type="checkbox"
                  checked={updatedProduct.featured}
                  onChange={(e) =>
                    setUpdatedProduct({
                      ...updatedProduct,
                      featured: e.target.checked,
                    })
                  }
                />
                <label className="text-sm font-medium text-gray-700">
                  Featured
                </label>
              </div>
              <div className="col-span-2">
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Product Description
                </label>
                <textarea
                  placeholder="Product Description"
                  value={updatedProduct.description}
                  onChange={(e) =>
                    setUpdatedProduct({
                      ...updatedProduct,
                      description: e.target.value,
                    })
                  }
                  className="w-full px-4 py-2 border rounded-md"
                  rows={6}
                />
              </div>
            </div>
            <div className="flex justify-end gap-4 mt-6">
              <button
                onClick={() => setIsModalOpen(false)} // Close the modal
                className="px-6 py-3 bg-gray-300 text-gray-800 rounded-md hover:bg-gray-400"
              >
                Cancel
              </button>
              <button
                onClick={handleUpdate} // Save changes
                className="px-6 py-3 bg-green-600 text-white rounded-md hover:bg-green-700"
              >
                Save
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default ProductCard;

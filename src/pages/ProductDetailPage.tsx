import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { api } from "../api";
import Navbar from "../components/Navbar";
import { Star } from "lucide-react";
import { useAuth } from "../context/AuthContext";
import StarRating from "../components/StarRating";
import RatingCard from "../components/RatingCard";
import { Product } from "../types/Product";
import { Rating } from "../types/Rating";
import { Category } from "../types/Category";

export default function ProductPage() {
  const { id } = useParams<{ id: string }>();
  const [product, setProduct] = useState<Product | null>(null);
  const [ratings, setRatings] = useState<Rating[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [reviewText, setReviewText] = useState("");
  const [reviewRating, setReviewRating] = useState(0);
  const [submitMessage, setSubmitMessage] = useState("");
  const { user } = useAuth();
  const [ratingError, setRatingError] = useState("");
  const [commentError, setCommentError] = useState("");
  const [selectedCategories, setSelectedCategories] = useState<number[]>([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);
  const [isPopupOpen, setIsPopupOpen] = useState(false);
  const [searchInput, setSearchInput] = useState("");
  const [ratingToDelete, setRatingToDelete] = useState<number | null>(null);
  const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);
  const [productCategories, setProductCategories] = useState<Category[] | null>(
    null
  );
  const [popupCategories, setPopupCategories] = useState<Category[]>([]);
  const [isPopupLoading, setIsPopupLoading] = useState(false);
  useEffect(() => {
    const fetchData = async () => {
      if (!id) return;

      setIsLoading(true);
      try {
        const productResponse = await api.products.getById(id);
        setProduct(productResponse.data);

        const ratingResponse = await api.ratings.getByProductId(id);
        setRatings(ratingResponse.content || []);
        try {
          const categoryResponse = await api.categories.getByProductId(id);
          setProductCategories(categoryResponse.categories || []);
        } catch {
          setProductCategories(null);
        }
      } catch (err) {
        setError("Failed to fetch product details.");
        console.error(err);
      } finally {
        setIsLoading(false);
      }
    };

    fetchData();
  }, [id]);

  const fetchCategories = async (page: number = 0, search: string = "") => {
    setIsPopupLoading(true);
    try {
      const response = await api.categories.getAll({
        page,
        size: 5,
        sort: "name",
        search,
      });
      setPopupCategories(response.content);
      setTotalPages(response.pageable.totalPages);
    } catch (error) {
      console.error("Failed to fetch categories:", error);
    } finally {
      setIsPopupLoading(false);
    }
  };

  const handleOpenPopup = () => {
    setIsPopupOpen(true);
    setSelectedCategories(productCategories?.map((cat) => cat.id) || []);
    fetchCategories();
  };

  const handleClosePopup = () => {
    setIsPopupOpen(false);
    setSearchInput("");
    setPopupCategories([]);
    setSelectedCategories([]);
    setCurrentPage(0);
  };

  const averageRating =
    ratings.length > 0
      ? ratings.reduce((sum, r) => sum + r.point, 0) / ratings.length
      : 0;

  const handleReviewSubmit = async () => {
    setRatingError("");
    setCommentError("");
    setSubmitMessage("");

    let hasError = false;

    if (!reviewRating) {
      setRatingError("Please select a star rating.");
      hasError = true;
    }

    if (!reviewText.trim()) {
      setCommentError("Please write a comment.");
      hasError = true;
    }

    if (hasError) return;

    try {
      const existingRating = ratings.find(
        (rating) => rating.userEmail === user?.email
      );

      if (existingRating) {
        await api.ratings.update(existingRating.id, {
          point: reviewRating,
          description: reviewText,
        });
        setSubmitMessage("Review updated successfully.");
      } else {
        await api.ratings.create({
          userId: user?.id,
          productId: Number(id),
          point: reviewRating,
          description: reviewText,
        });
        setSubmitMessage("Thank you for your review!");
      }
      const ratingResponse = await api.ratings.getByProductId(id!);
      setRatings(ratingResponse.content || []);
      setReviewText("");
      setReviewRating(0);
    } catch (err) {
      console.error("Error submitting review:", err);
      setSubmitMessage("Error submitting review.");
    }
  };

  const handleCancel = () => {
    setReviewText("");
    setReviewRating(0);
    setRatingError("");
    setCommentError("");
    setSubmitMessage("");
  };

  const handleUpdateClick = (rating: Rating) => {
    setReviewText(rating.description);
    setReviewRating(rating.point);
    setSubmitMessage("");
    setRatingError("");
    setCommentError("");
  };

  const handleDeleteClick = (ratingId: number) => {
    setRatingToDelete(ratingId); // Set the rating to delete
    setIsDeleteModalOpen(true); // Open the delete confirmation modal
  };

  const handleCategorySelect = (categoryId: number) => {
    setSelectedCategories((prev) =>
      prev.includes(categoryId)
        ? prev.filter((id) => id !== categoryId)
        : [...prev, categoryId]
    );
  };
  const confirmDelete = async () => {
    if (!ratingToDelete) return;

    try {
      await api.ratings.delete(ratingToDelete);
      setRatings((prevRatings) =>
        prevRatings.filter((rating) => rating.id !== ratingToDelete)
      );
      setSubmitMessage("Review deleted successfully.");
    } catch (err) {
      console.error("Error deleting review:", err);
      setSubmitMessage("Error deleting review.");
    } finally {
      setRatingToDelete(null); // Reset the rating to delete
      setIsDeleteModalOpen(false); // Close the modal
    }
  };
  const handleAssignCategories = async () => {
    if (!id) return;

    try {
      await api.products.assignCategoriesToProduct(
        Number(id),
        selectedCategories
      );

      const categoryResponse = await api.categories.getByProductId(id);
      setProductCategories(categoryResponse.categories || []);

      handleClosePopup();
    } catch (error) {
      console.error("Failed to assign categories:", error);
      alert("Failed to assign categories. Please try again.");
    }
  };

  const handlePageChange = (page: number) => {
    setCurrentPage(page);
    fetchCategories(page, searchInput);
  };

  const handleSearch = () => {
    fetchCategories(0, searchInput);
  };

  return (
    <div className="min-h-screen flex flex-col">
      <Navbar />

      <main className="flex-1 container mx-auto px-4 py-10">
        {isLoading ? (
          <p className="text-center text-lg text-gray-600">Loading...</p>
        ) : error ? (
          <div className="text-red-600 text-center text-lg">{error}</div>
        ) : product ? (
          <div className="space-y-12">
            {/* Product Info */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-10">
              <div className="bg-white shadow-lg rounded-xl overflow-hidden">
                {product.image ? (
                  <img
                    src={product.image}
                    alt={product.name}
                    className="w-full object-cover"
                  />
                ) : (
                  <div className="h-64 bg-gray-100 flex flex-col items-center justify-center">
                    <span className="text-gray-400 mb-4">
                      No image available
                    </span>
                    {user?.role === "ADMIN" && (
                      <>
                        <label
                          htmlFor="upload-image"
                          className="px-4 py-2 bg-indigo-600 text-white rounded-md hover:bg-indigo-700 cursor-pointer"
                        >
                          Upload Image
                        </label>
                        <input
                          type="file"
                          id="upload-image"
                          accept="image/*"
                          className="hidden"
                          onChange={async (e) => {
                            if (e.target.files && e.target.files[0]) {
                              const file = e.target.files[0];

                              try {
                                const response = await api.products.uploadImage(
                                  product.id,
                                  file
                                );

                                if (response.ok) {
                                  const data = await response.json();
                                  alert("Image uploaded successfully!");
                                  setProduct((prev) =>
                                    prev ? { ...prev, image: data.data } : prev
                                  ); // Update the product image
                                } else {
                                  const errorData = await response.json();
                                  alert(
                                    `Failed to upload image: ${errorData.message}`
                                  );
                                }
                              } catch (error) {
                                console.error("Error uploading image:", error);
                                alert(
                                  "An error occurred while uploading the image."
                                );
                              }
                            }
                          }}
                        />
                      </>
                    )}
                  </div>
                )}
              </div>

              <div className="space-y-4">
                <h1 className="text-4xl font-bold text-gray-900">
                  {product.name}
                </h1>
                <div className="flex items-center">
                  <StarRating rating={averageRating} />
                  <span className="ml-3 text-sm text-gray-600">
                    {averageRating > 0
                      ? `${averageRating.toFixed(1)} (${ratings.length} ${
                          ratings.length === 1 ? "review" : "reviews"
                        })`
                      : "No ratings yet"}
                  </span>
                </div>
                <p className="text-2xl font-semibold text-blue-600">
                  ${product.price}
                </p>
                <p className="text-gray-700 text-lg">{product.description}</p>
                {/* Categories */}
                {productCategories && productCategories.length > 0 ? (
                  <div>
                    <h3 className="text-lg font-semibold text-gray-800 mb-2">
                      Categories
                    </h3>
                    <div className="flex flex-wrap gap-2">
                      {productCategories.map((category) => (
                        <span
                          key={category.id}
                          className="inline-block bg-indigo-100 text-indigo-800 text-sm font-medium px-3 py-1 rounded-full shadow-sm hover:bg-indigo-200 transition-colors"
                        >
                          {category.name}
                        </span>
                      ))}
                    </div>
                  </div>
                ) : (
                  <p className="text-gray-500">No categories available</p>
                )}
                {/* Assign Categories Button */}
                {user?.role === "ADMIN" && (
                  <button
                    onClick={handleOpenPopup}
                    className="px-4 py-2 bg-indigo-600 text-white rounded-md hover:bg-indigo-700"
                  >
                    Assign Categories
                  </button>
                )}

                {/* Popup Modal */}
                {isPopupOpen && (
                  <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
                    <div className="bg-white p-6 rounded-lg shadow-lg w-full max-w-lg">
                      <h2 className="text-xl font-bold mb-4">
                        Assign Categories
                      </h2>

                      {/* Search Bar */}
                      <div className="flex gap-2 mb-4">
                        <input
                          type="text"
                          placeholder="Search categories..."
                          value={searchInput}
                          onChange={(e) => setSearchInput(e.target.value)}
                          className="flex-grow px-4 py-2 border rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
                        />
                        <button
                          onClick={handleSearch}
                          className="px-4 py-2 bg-indigo-600 text-white rounded-md hover:bg-indigo-700"
                        >
                          Search
                        </button>
                      </div>

                      {/* Categories List */}
                      <div
                        className="space-y-2 overflow-hidden"
                        style={{
                          height: "250px",
                        }}
                      >
                        <div
                          className="space-y-2 overflow-hidden"
                          style={{
                            height: "250px",
                          }}
                        >
                          {isPopupLoading ? (
                            <p>Loading categories...</p>
                          ) : (
                            <>
                              {popupCategories.map((category) => (
                                <label
                                  key={category.id}
                                  className="flex items-center space-x-2 h-10"
                                >
                                  <input
                                    type="checkbox"
                                    value={category.id}
                                    checked={selectedCategories.includes(
                                      category.id
                                    )}
                                    onChange={() =>
                                      handleCategorySelect(category.id)
                                    }
                                    className="form-checkbox"
                                  />
                                  <span>{category.name}</span>
                                </label>
                              ))}
                              {/* Add placeholders to fill the remaining space */}
                              {popupCategories.length < 5 &&
                                Array.from({
                                  length: 5 - popupCategories.length,
                                }).map((_, index) => (
                                  <div
                                    key={`placeholder-${index}`}
                                    className="h-10"
                                  />
                                ))}
                            </>
                          )}
                        </div>
                      </div>

                      {/* Pagination Controls */}
                      <div className="flex justify-between items-center mt-4">
                        <button
                          type="button"
                          onClick={() => handlePageChange(currentPage - 1)}
                          disabled={currentPage === 0}
                          className="px-4 py-2 bg-gray-300 text-gray-800 rounded-md hover:bg-gray-400 disabled:opacity-50"
                        >
                          Previous
                        </button>
                        <span>
                          Page {currentPage + 1} of {totalPages}
                        </span>
                        <button
                          type="button"
                          onClick={() => handlePageChange(currentPage + 1)}
                          disabled={currentPage === totalPages - 1}
                          className="px-4 py-2 bg-gray-300 text-gray-800 rounded-md hover:bg-gray-400 disabled:opacity-50"
                        >
                          Next
                        </button>
                      </div>

                      {/* Action Buttons */}
                      <div className="flex justify-end gap-4 mt-6">
                        <button
                          onClick={handleClosePopup}
                          className="px-4 py-2 bg-gray-300 text-gray-800 rounded-md hover:bg-gray-400"
                        >
                          Cancel
                        </button>
                        <button
                          onClick={handleAssignCategories}
                          disabled={selectedCategories.length === 0}
                          className={`px-4 py-2 rounded-md ${
                            selectedCategories.length > 0
                              ? "bg-indigo-600 text-white hover:bg-indigo-700"
                              : "bg-gray-300 text-gray-500 cursor-not-allowed"
                          }`}
                        >
                          Assign
                        </button>
                      </div>
                    </div>
                  </div>
                )}
              </div>
            </div>
            <div>
              <h2 className="text-2xl font-semibold text-gray-800 mb-6">
                Customer Reviews
              </h2>

              {/* Review Form */}
              <div className="bg-gray-50 p-6 rounded-lg shadow-sm border mb-10">
                <h3 className="text-lg font-semibold text-gray-700 mb-4">
                  Write a Review
                </h3>

                <div className="mb-3 flex gap-1">
                  {[1, 2, 3, 4, 5].map((star) => (
                    <button
                      key={star}
                      onClick={() => setReviewRating(star)}
                      className="hover:scale-110 transition-transform"
                    >
                      <Star
                        className={`w-6 h-6 ${
                          star <= reviewRating
                            ? "fill-yellow-400 text-yellow-400"
                            : "text-gray-300"
                        }`}
                      />
                    </button>
                  ))}
                </div>
                {ratingError && (
                  <p className="text-red-500 text-sm">{ratingError}</p>
                )}

                <textarea
                  value={reviewText}
                  onChange={(e) => setReviewText(e.target.value)}
                  rows={4}
                  placeholder="Share your thoughts..."
                  className="w-full mt-2 border border-gray-300 rounded-md p-3 text-sm focus:outline-none focus:ring focus:ring-blue-200"
                />
                {commentError && (
                  <p className="text-red-500 text-sm mt-1">{commentError}</p>
                )}

                <div className="text-right mt-3 space-x-4">
                  {/* Cancel Button */}
                  <button
                    onClick={handleCancel}
                    className="bg-gray-300 hover:bg-gray-400 text-gray-800 px-5 py-2 rounded-md text-sm"
                  >
                    Cancel
                  </button>

                  {/* Submit Button */}
                  <button
                    onClick={handleReviewSubmit}
                    className="bg-blue-600 hover:bg-blue-700 text-white px-5 py-2 rounded-md text-sm"
                  >
                    Submit Review
                  </button>
                </div>

                {submitMessage && (
                  <p className="mt-4 text-sm text-green-600">{submitMessage}</p>
                )}

                {/* No reviews message */}
                {ratings.length === 0 && (
                  <p className="text-gray-500 text-sm mt-6">No reviews yet.</p>
                )}
              </div>
              {isDeleteModalOpen && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
                  <div className="bg-white p-6 rounded-lg shadow-lg w-full max-w-md">
                    <h2 className="text-xl font-bold mb-4">Confirm Deletion</h2>
                    <p className="text-gray-600 mb-6">
                      Are you sure you want to delete this review? This action
                      cannot be undone.
                    </p>
                    <div className="flex justify-end gap-4">
                      <button
                        onClick={() => setIsDeleteModalOpen(false)} // Close the modal
                        className="px-4 py-2 bg-gray-300 text-gray-800 rounded-md hover:bg-gray-400"
                      >
                        Cancel
                      </button>
                      <button
                        onClick={confirmDelete} // Confirm deletion
                        className="px-4 py-2 bg-red-600 text-white rounded-md hover:bg-red-700"
                      >
                        Delete
                      </button>
                    </div>
                  </div>
                </div>
              )}
              {/* Reviews List */}
              {ratings.length > 0 && (
                <div className="space-y-6">
                  {ratings.map((rating) => (
                    <RatingCard
                      key={rating.id}
                      rating={rating}
                      user={user}
                      onUpdate={handleUpdateClick}
                      onDelete={handleDeleteClick}
                    />
                  ))}
                </div>
              )}
            </div>
          </div>
        ) : (
          <p className="text-center text-gray-500 text-lg">Product not found</p>
        )}
      </main>
    </div>
  );
}

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${product.name} - KeyCraft</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .star-rating {
            color: #ffc107;
        }

        .star-rating-display .fa-star {
            color: #ffc107;
        }

        .star-rating-display .fa-star-o {
            color: #e4e5e9;
        }

        .rating-bar {
            height: 10px;
            background-color: #e9ecef;
            border-radius: 5px;
            overflow: hidden;
        }

        .rating-fill {
            height: 100%;
            background-color: #ffc107;
        }
    </style>
</head>
<body>
<!-- Navigation -->
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container">
        <a class="navbar-brand" href="/"><i class="fas fa-keyboard"></i> KeyCraft</a>
        <div class="navbar-nav ms-auto">
            <a class="nav-link" href="/products">Products</a>
            <c:if test="${user != null}">
                <a class="nav-link" href="/cart">
                    <i class="fas fa-shopping-cart"></i> Cart
                    <span class="badge bg-primary" id="cart-count">${cartItemCount}</span>
                </a>
                <a class="nav-link" href="/orders">Orders</a>
                <a class="nav-link" href="/logout">Logout</a>
            </c:if>
            <c:if test="${user == null}">
                <a class="nav-link" href="/auth/login">Login</a>
            </c:if>
        </div>
    </div>
</nav>

<div class="container mt-4">
    <div class="row">
        <!-- Product Information -->
        <div class="col-md-6">
            <img src="${product.imageUrl}" alt="${product.name}" class="img-fluid rounded">
        </div>

        <div class="col-md-6">
            <h1>${product.name}</h1>
            <p class="text-muted">${product.brand} - ${product.category}</p>

            <!-- Rating Display -->
            <div class="d-flex align-items-center mb-3">
                <div class="star-rating-display me-2">
                    <c:forEach begin="1" end="5" var="i">
                        <c:choose>
                            <c:when test="${i <= reviewStats.averageRating}">
                                <i class="fas fa-star"></i>
                            </c:when>
                            <c:otherwise>
                                <i class="far fa-star"></i>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </div>
                <span class="me-2">
                        <fmt:formatNumber value="${reviewStats.averageRating}" maxFractionDigits="1"/>
                    </span>
                <span class="text-muted">(${reviewStats.totalReviews} reviews)</span>
            </div>

            <h3 class="text-primary">
                <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="$"/>
            </h3>

            <p class="mt-3">${product.description}</p>

            <!-- Product Details -->
            <div class="row mt-4">
                <div class="col-sm-6">
                    <strong>Switch Type:</strong> ${product.switchType}<br>
                    <strong>Layout:</strong> ${product.layout}
                </div>
                <div class="col-sm-6">
                    <strong>Stock:</strong>
                    <c:choose>
                        <c:when test="${product.stock > 0}">
                            <span class="text-success">${product.stock} available</span>
                        </c:when>
                        <c:otherwise>
                            <span class="text-danger">Out of stock</span>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <!-- Add to Cart -->
            <c:if test="${user != null && product.stock > 0}">
                <div class="mt-4">
                    <div class="row">
                        <div class="col-4">
                            <input type="number" id="quantity" class="form-control" value="1" min="1"
                                   max="${product.stock}">
                        </div>
                        <div class="col-8">
                            <c:choose>
                                <c:when test="${inCart}">
                                    <button class="btn btn-success w-100" disabled>
                                        <i class="fas fa-check"></i> In Cart
                                    </button>
                                </c:when>
                                <c:otherwise>
                                    <button class="btn btn-primary w-100" onclick="addToCart(${product.id})">
                                        <i class="fas fa-cart-plus"></i> Add to Cart
                                    </button>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </c:if>

            <c:if test="${user == null}">
                <div class="mt-4">
                    <a href="/auth/login" class="btn btn-primary w-100">Login to Purchase</a>
                </div>
            </c:if>
        </div>
    </div>

    <!-- Reviews Section -->
    <div class="row mt-5">
        <div class="col-12">
            <h3>Customer Reviews</h3>

            <!-- Rating Summary -->
            <div class="row mb-4">
                <div class="col-md-3">
                    <div class="text-center">
                        <h2><fmt:formatNumber value="${reviewStats.averageRating}" maxFractionDigits="1"/></h2>
                        <div class="star-rating-display mb-2">
                            <c:forEach begin="1" end="5" var="i">
                                <c:choose>
                                    <c:when test="${i <= reviewStats.averageRating}">
                                        <i class="fas fa-star"></i>
                                    </c:when>
                                    <c:otherwise>
                                        <i class="far fa-star"></i>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </div>
                        <p class="text-muted">${reviewStats.totalReviews} reviews</p>
                    </div>
                </div>
                <div class="col-md-9">
                    <c:forEach begin="1" end="5" var="star">
                        <div class="d-flex align-items-center mb-1">
                            <span class="me-2">${6-star} star</span>
                            <div class="rating-bar flex-grow-1 me-2">
                                <div class="rating-fill"
                                     style="width: ${reviewStats.getStarCount(6-star) * 100 / (reviewStats.totalReviews > 0 ? reviewStats.totalReviews : 1)}%"></div>
                            </div>
                            <span class="text-muted">${reviewStats.getStarCount(6-star)}</span>
                        </div>
                    </c:forEach>
                </div>
            </div>

            <!-- Write Review -->
            <c:if test="${user != null && hasPurchased && !hasReviewed}">
                <div class="card mb-4">
                    <div class="card-header">
                        <h5>Write a Review</h5>
                        <small class="text-success"><i class="fas fa-check-circle"></i> Verified Purchase</small>
                    </div>
                    <div class="card-body">
                        <form id="review-form" data-product-id="${product.id}">
                            <div class="mb-3">
                                <label class="form-label">Your Rating</label>
                                <div class="star-rating" id="rating-input">
                                    <i class="far fa-star" data-rating="1"></i>
                                    <i class="far fa-star" data-rating="2"></i>
                                    <i class="far fa-star" data-rating="3"></i>
                                    <i class="far fa-star" data-rating="4"></i>
                                    <i class="far fa-star" data-rating="5"></i>
                                </div>
                                <input type="hidden" id="rating" name="rating" value="0">
                            </div>
                            <div class="mb-3">
                                <label for="comment" class="form-label">Your Review</label>
                                <textarea class="form-control" id="comment" rows="4"
                                          placeholder="Share your thoughts..."></textarea>
                            </div>
                            <button type="submit" class="btn btn-primary">Submit Review</button>
                        </form>
                    </div>
                </div>
            </c:if>

            <!-- Reviews List -->
            <div id="reviews-list">
                <c:forEach var="review" items="${reviews}">
                    <div class="card mb-3">
                        <div class="card-body">
                            <div class="d-flex justify-content-between align-items-start">
                                <div>
                                    <h6 class="card-title">
                                            ${review.userFullName}
                                        <c:if test="${review.verified}">
                                            <small class="text-success"><i class="fas fa-check-circle"></i> Verified
                                                Purchase</small>
                                        </c:if>
                                    </h6>
                                    <div class="star-rating-display mb-2">
                                        <c:forEach begin="1" end="5" var="i">
                                            <c:choose>
                                                <c:when test="${i <= review.rating}">
                                                    <i class="fas fa-star"></i>
                                                </c:when>
                                                <c:otherwise>
                                                    <i class="far fa-star"></i>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </div>
                                </div>
                                <small class="text-muted">
                                        ${review.createdAtFormatted}
                                </small>
                            </div>
                            <p class="card-text">${review.comment}</p>
                        </div>
                    </div>
                </c:forEach>

                <c:if test="${empty reviews}">
                    <div class="text-center py-4">
                        <i class="fas fa-comments fa-3x text-muted mb-3"></i>
                        <h5>No reviews yet</h5>
                        <p class="text-muted">Be the first to review this product!</p>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
</div>

<!-- Notification Toast -->
<div class="toast-container position-fixed bottom-0 end-0 p-3">
    <div id="notification-toast" class="toast" role="alert">
        <div class="toast-header">
            <strong class="me-auto">KeyCraft</strong>
            <button type="button" class="btn-close" data-bs-dismiss="toast"></button>
        </div>
        <div class="toast-body" id="toast-message"></div>
    </div>
</div>

<script>
    let selectedRating = 0;
    const ratingStars = document.querySelectorAll('#rating-input i');

    ratingStars.forEach(star => {
        star.addEventListener('mouseover', () => {
            highlightStars(parseInt(star.dataset.rating));
        });
        star.addEventListener('mouseout', () => {
            highlightStars(selectedRating);
        });
        star.addEventListener('click', () => {
            selectedRating = parseInt(star.dataset.rating);
            document.getElementById('rating').value = selectedRating;
            highlightStars(selectedRating);
        });
    });

    function highlightStars(rating) {
        ratingStars.forEach(star => {
            const r = parseInt(star.dataset.rating);
            if (r <= rating) {
                star.classList.remove('far');
                star.classList.add('fas');
            } else {
                star.classList.remove('fas');
                star.classList.add('far');
            }
        });
    }

    // Submit review
    document.getElementById("review-form")?.addEventListener("submit", function (e) {
        e.preventDefault();
        const comment = document.getElementById("comment").value.trim();
        const productId = this.dataset.productId;

        if (!selectedRating || selectedRating < 1 || selectedRating > 5) {
            showToast("Please select a rating", "error");
            return;
        }

        if (!comment) {
            showToast("Please enter your review", "error");
            return;
        }

        fetch("/reviews/create", {
            method: "POST",
            headers: {"Content-Type": "application/x-www-form-urlencoded"},
            body: new URLSearchParams({productId, rating: selectedRating, comment})
        })
            .then(res => res.json())
            .then(data => {
                showToast(data.message || "Review submitted", data.success ? 'success' : 'error');
                if (data.success) {
                    setTimeout(() => window.location.reload(), 1000);
                }
            })
            .catch(err => {
                console.error(err);
                showToast("Error submitting review", "error");
            });
    });

    // Add to cart
    function addToCart(productId, quantity = 1) {
        fetch('/cart/add', {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: new URLSearchParams({productId, quantity})
        })
            .then(async res => {
                if (!res.ok) {
                    throw new Error('Server error: ' + res.status);
                }
                let data;
                try {
                    data = await res.json();
                } catch (e) {
                    throw new Error('Invalid JSON');
                }

                if (data.success) {
                    showToast(data.message, 'success');
                    updateCartBadge(data.cartItemCount);
                } else {
                    showToast(data.message || 'Thêm sản phẩm thất bại', 'error');
                }
            })
            .catch(err => {
                console.error(err);
                showToast('Có lỗi xảy ra khi thêm sản phẩm vào giỏ hàng', 'error');
            });
    }

    // Toast
    function showToast(message, type = 'info') {
        const toastMessage = document.getElementById("toast-message");
        toastMessage.textContent = message;
        toastMessage.className = 'toast-body'; // reset
        toastMessage.classList.add(
            type === 'success' ? 'text-success' :
                type === 'error' ? 'text-danger' : 'text-info'
        );
        const toast = new bootstrap.Toast(document.getElementById("notification-toast"));
        toast.show();
    }

    function updateCartBadge(count) { // Đặt breakpoint tại đây
        debugger;
        const cartCountElement = document.getElementById("cart-count");
        if (cartCountElement) {
            cartCountElement.textContent = count;
        }
    }
</script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
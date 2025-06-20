<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Products - KeyCraft</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="/webjars/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .product-card {
            transition: transform 0.3s;
        }

        .product-card:hover {
            transform: translateY(-5px);
        }

        .filter-sidebar {
            background: #f8f9fa;
            border-radius: 10px;
            padding: 20px;
        }
    </style>
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container">
        <a class="navbar-brand" href="/"><i class="fas fa-keyboard"></i> KeyCraft</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto">
                <li class="nav-item"><a class="nav-link" href="/">Home</a></li>
                <li class="nav-item"><a class="nav-link active" href="/products">Products</a></li>
                <c:if test="${currentUser != null && currentUser.role == 'ADMIN'}">
                    <li class="nav-item"><a class="nav-link" href="/admin">Admin Panel</a></li>
                </c:if>
                <c:if test="${not empty currentUser}">
                    <li class="nav-item">
                        <a class="nav-link" href="/cart">
                            <i class="fas fa-shopping-cart"></i> Cart

                            <span id="cart-count" class="badge bg-primary">${cartItemCount}</span>

                        </a>
                    </li>
                </c:if>
            </ul>
            <ul class="navbar-nav">
                <c:choose>
                    <c:when test="${currentUser != null}">
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" href="#" data-bs-toggle="dropdown">
                                <i class="fas fa-user"></i> ${currentUser.firstName} ${currentUser.lastName}
                            </a>
                            <ul class="dropdown-menu">
                                <li><a class="dropdown-item" href="/auth/logout">Logout</a></li>
                            </ul>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item"><a class="nav-link" href="/login">Login</a></li>
                        <li class="nav-item"><a class="nav-link" href="/signup">Sign Up</a></li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>

<div class="container my-4">
    <div class="row">
        <!-- Sidebar Filters -->
        <div class="col-lg-3 mb-4">
            <div class="filter-sidebar">
                <h5><i class="fas fa-filter"></i> Filters</h5>
                <div class="mb-3">
                    <label>Search</label>
                    <input type="text" class="form-control" id="searchInput" placeholder="Search products...">
                </div>
                <div class="mb-3">
                    <label>Category</label>
                    <select class="form-select" id="categoryFilter">
                        <option value="">All Categories</option>
                        <option value="mechanical-keyboards">Mechanical Keyboards</option>
                        <option value="switches">Switches</option>
                        <option value="keycaps">Keycaps</option>
                        <option value="accessories">Accessories</option>
                    </select>
                </div>
                <div class="mb-3">
                    <label>Brand</label>
                    <select class="form-select" id="brandFilter">
                        <option value="">All Brands</option>
                        <option value="Keychron">Keychron</option>
                        <option value="Ducky">Ducky</option>
                        <option value="Leopold">Leopold</option>
                        <option value="Cherry">Cherry</option>
                        <option value="Varmilo">Varmilo</option>
                        <option value="HHKB">HHKB</option>
                    </select>
                </div>
                <div class="mb-3">
                    <label>Price Range</label>
                    <div class="row">
                        <div class="col-6"><input type="number" class="form-control" placeholder="Min" id="minPrice">
                        </div>
                        <div class="col-6"><input type="number" class="form-control" placeholder="Max" id="maxPrice">
                        </div>
                    </div>
                </div>
                <button class="btn btn-primary w-100" onclick="applyFilters()">
                    <i class="fas fa-search"></i> Apply Filters
                </button>
            </div>
        </div>

        <!-- Product Grid -->
        <div class="col-lg-9">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2>Our Products</h2>
                <span class="text-muted">${products.size()} products found</span>
            </div>
            <div class="row" id="productsGrid">
                <c:forEach items="${products}" var="product">
                    <div class="col-md-6 col-lg-4 mb-4 product-item"
                         data-category="${product.category}"
                         data-brand="${product.brand}"
                         data-price="${product.price}"
                         data-name="${product.name}">
                        <div class="card product-card h-100">
                            <a href="/product/${product.id}" class="text-decoration-none text-dark">
                                <img src="${product.imageUrl}" class="card-img-top" alt="${product.name}"
                                     style="height: 200px; object-fit: cover;">
                            </a>
                            <div class="card-body d-flex flex-column">
                                <a href="/product/${product.id}" class="text-decoration-none text-dark">
                                    <h5 class="card-title">${product.name}</h5>
                                </a>
                                <p class="text-muted small">${product.brand}</p>
                                <p class="flex-grow-1">${product.description}</p>
                                <div class="mt-auto">
                                    <div class="d-flex justify-content-between align-items-center mb-2">
                                        <span class="h5 text-primary">$${product.price}</span>
                                        <small class="text-success">
                                            <i class="fas fa-box"></i> ${product.stock} in stock
                                        </small>
                                    </div>
                                    <c:if test="${product.switchType != null}">
                                        <small class="text-muted d-block mb-2"><i
                                                class="fas fa-cog"></i> ${product.switchType} switches</small>
                                    </c:if>
                                    <c:if test="${product.featured}">
                                        <span class="badge bg-warning text-dark mb-2">Featured</span>
                                    </c:if>
                                    <button class="btn btn-primary w-100" onclick="addToCart(${product.id})">
                                        <i class="fas fa-cart-plus"></i> Add to Cart
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
            <div id="noResults" class="text-center py-5" style="display: none;">
                <i class="fas fa-search fa-3x text-muted mb-3"></i>
                <h4>No products found</h4>
                <p class="text-muted">Try adjusting your filters or search terms.</p>
            </div>
        </div>
    </div>
</div>

<!-- Toast container -->
<div class="toast-container position-fixed top-0 end-0 p-3">
    <div id="notification-toast" class="toast" role="alert" aria-live="assertive" aria-atomic="true">
        <div class="toast-header">
            <strong class="me-auto">KeyCraft</strong>
            <button type="button" class="btn-close" data-bs-dismiss="toast" aria-label="Close"></button>
        </div>
        <div class="toast-body" id="toast-message"></div>
    </div>
</div>
<footer class="bg-dark text-white py-4 mt-5">
    <div class="container text-center">
        <p>&copy; 2025 KeyCraft. All rights reserved.</p>
    </div>
</footer>

<script src="/webjars/jquery/jquery.min.js"></script>
<script src="/webjars/bootstrap/js/bootstrap.bundle.min.js"></script>
<script>
    function applyFilters() {
        const searchTerm = $('#searchInput').val().toLowerCase();
        const category = $('#categoryFilter').val();
        const brand = $('#brandFilter').val();
        const minPrice = parseFloat($('#minPrice').val()) || 0;
        const maxPrice = parseFloat($('#maxPrice').val()) || Infinity;

        let visibleCount = 0;

        $('.product-item').each(function () {
            const $item = $(this);
            const name = $item.data('name').toLowerCase();
            const categoryVal = $item.data('category');
            const brandVal = $item.data('brand');
            const price = parseFloat($item.data('price'));

            let show = true;

            if (searchTerm && !name.includes(searchTerm)) show = false;
            if (category && categoryVal !== category) show = false;
            if (brand && brandVal !== brand) show = false;
            if (price < minPrice || price > maxPrice) show = false;

            $item.toggle(show);
            if (show) visibleCount++;
        });

        $('#noResults').toggle(visibleCount === 0);
    }

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

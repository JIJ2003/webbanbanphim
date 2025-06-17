<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Panel - KeyCraft</title>
    <link href="/webjars/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>
    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container">
            <a class="navbar-brand" href="/">
                <i class="fas fa-keyboard"></i> KeyCraft Admin
            </a>
            <div class="navbar-nav ms-auto">
                <a class="nav-link" href="/"><i class="fas fa-home"></i> Back to Store</a>
                <a class="nav-link" href="/auth/logout"><i class="fas fa-sign-out-alt"></i> Logout</a>
            </div>
        </div>
    </nav>

    <div class="container my-4">
        <h1 class="mb-4"><i class="fas fa-cogs"></i> Admin Dashboard <small class="text-muted">Welcome, ${currentUser.firstName}!</small></h1>

        <!-- Product Management -->
        <div class="card">
            <div class="card-header d-flex justify-content-between align-items-center">
                <h5 class="mb-0"><i class="fas fa-boxes"></i> Product Management</h5>
                <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addProductModal">
                    <i class="fas fa-plus"></i> Add Product
                </button>
            </div>
            <div class="card-body">
                <div class="table-responsive">
                    <table class="table table-striped">
                        <thead>
                            <tr>
                                <th>Image</th>
                                <th>Name</th>
                                <th>Brand</th>
                                <th>Price</th>
                                <th>Stock</th>
                                <th>Featured</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${products}" var="product">
                                <tr>
                                    <td><img src="${product.imageUrl}" alt="${product.name}" style="width: 50px; height: 50px; object-fit: cover;" class="rounded"></td>
                                    <td>${product.name}</td>
                                    <td>${product.brand}</td>
                                    <td>$${product.price}</td>
                                    <td>
                                        <span class="badge ${product.stock > 10 ? 'bg-success' : product.stock > 0 ? 'bg-warning' : 'bg-danger'}">
                                            ${product.stock}
                                        </span>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${product.featured}">
                                                <span class="badge bg-primary">Featured</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-secondary">Regular</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <button class="btn btn-sm btn-outline-primary" onclick="editProduct(${product.id})"><i class="fas fa-edit"></i></button>
                                        <button class="btn btn-sm btn-outline-danger" onclick="deleteProduct(${product.id})"><i class="fas fa-trash"></i></button>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <!-- Add Product Modal -->
    <div class="modal fade" id="addProductModal" tabindex="-1">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <form id="addProductForm" method="post">
                    <div class="modal-header">
                        <h5 class="modal-title">Add New Product</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="name" class="form-label">Product Name</label>
                                <input type="text" class="form-control" id="name" name="name" required>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="brand" class="form-label">Brand</label>
                                <input type="text" class="form-control" id="brand" name="brand" required>
                            </div>
                        </div>
                        <div class="mb-3">
                            <label for="description" class="form-label">Description</label>
                            <textarea class="form-control" id="description" name="description" rows="3" required></textarea>
                        </div>
                        <div class="row">
                            <div class="col-md-4 mb-3">
                                <label for="price" class="form-label">Price</label>
                                <input type="number" step="0.01" class="form-control" id="price" name="price" required>
                            </div>
                            <div class="col-md-4 mb-3">
                                <label for="stock" class="form-label">Stock</label>
                                <input type="number" class="form-control" id="stock" name="stock" required>
                            </div>
                            <div class="col-md-4 mb-3">
                                <label for="category" class="form-label">Category</label>
                                <select class="form-select" id="category" name="category" required>
                                    <option value="mechanical-keyboards">Mechanical Keyboards</option>
                                    <option value="switches">Switches</option>
                                    <option value="keycaps">Keycaps</option>
                                    <option value="accessories">Accessories</option>
                                </select>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="switchType" class="form-label">Switch Type</label>
                                <select class="form-select" id="switchType" name="switchType">
                                    <option value="">Select switch type</option>
                                    <option value="linear">Linear</option>
                                    <option value="tactile">Tactile</option>
                                    <option value="clicky">Clicky</option>
                                </select>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="layout" class="form-label">Layout</label>
                                <input type="text" class="form-control" id="layout" name="layout" placeholder="e.g., 60%, TKL, Full">
                            </div>
                        </div>
                        <div class="mb-3">
                            <label for="imageUrl" class="form-label">Image URL</label>
                            <input type="url" class="form-control" id="imageUrl" name="imageUrl" required>
                        </div>
                        <div class="mb-3 form-check">
                            <input type="checkbox" class="form-check-input" id="featured" name="featured">
                            <label class="form-check-label" for="featured">Featured Product</label>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-primary">Add Product</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Edit Product Modal -->
    <div class="modal fade" id="editProductModal" tabindex="-1">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <form id="editProductForm">
                    <div class="modal-header">
                        <h5 class="modal-title">Edit Product</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <input type="hidden" id="editProductId" name="id">
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="editName" class="form-label">Product Name</label>
                                <input type="text" class="form-control" id="editName" name="name" required>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="editBrand" class="form-label">Brand</label>
                                <input type="text" class="form-control" id="editBrand" name="brand" required>
                            </div>
                        </div>
                        <div class="mb-3">
                            <label for="editDescription" class="form-label">Description</label>
                            <textarea class="form-control" id="editDescription" name="description" rows="3" required></textarea>
                        </div>
                        <div class="row">
                            <div class="col-md-4 mb-3">
                                <label for="editPrice" class="form-label">Price</label>
                                <input type="number" step="0.01" class="form-control" id="editPrice" name="price" required>
                            </div>
                            <div class="col-md-4 mb-3">
                                <label for="editStock" class="form-label">Stock</label>
                                <input type="number" class="form-control" id="editStock" name="stock" required>
                            </div>
                            <div class="col-md-4 mb-3">
                                <label for="editCategory" class="form-label">Category</label>
                                <select class="form-select" id="editCategory" name="category" required>
                                    <option value="mechanical-keyboards">Mechanical Keyboards</option>
                                    <option value="switches">Switches</option>
                                    <option value="keycaps">Keycaps</option>
                                    <option value="accessories">Accessories</option>
                                </select>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="editSwitchType" class="form-label">Switch Type</label>
                                <select class="form-select" id="editSwitchType" name="switchType">
                                    <option value="">Select switch type</option>
                                    <option value="linear">Linear</option>
                                    <option value="tactile">Tactile</option>
                                    <option value="clicky">Clicky</option>
                                </select>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="editLayout" class="form-label">Layout</label>
                                <input type="text" class="form-control" id="editLayout" name="layout">
                            </div>
                        </div>
                        <div class="mb-3">
                            <label for="editImageUrl" class="form-label">Image URL</label>
                            <input type="url" class="form-control" id="editImageUrl" name="imageUrl" required>
                        </div>
                        <div class="mb-3 form-check">
                            <input type="checkbox" class="form-check-input" id="editFeatured" name="featured">
                            <label class="form-check-label" for="editFeatured">Featured Product</label>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-primary">Save Changes</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- JS -->
    <script src="/webjars/jquery/jquery.min.js"></script>
<script src="/webjars/bootstrap/js/bootstrap.bundle.min.js"></script>
<script>
    $(document).ready(function () {
        // Edit product
        window.editProduct = function (id) {
            $.ajax({
                url: '/api/products/' + id,
                method: 'GET',
                xhrFields: {
                    withCredentials: true
                },
                success: function (product) {
                    $('#editProductId').val(product.id);
                    $('#editName').val(product.name);
                    $('#editBrand').val(product.brand);
                    $('#editDescription').val(product.description);
                    $('#editPrice').val(product.price);
                    $('#editStock').val(product.stock);
                    $('#editCategory').val(product.category);
                    $('#editSwitchType').val(product.switchType || '');
                    $('#editLayout').val(product.layout || '');
                    $('#editImageUrl').val(product.imageUrl);
                    $('#editFeatured').prop('checked', product.featured);

                    new bootstrap.Modal(document.getElementById('editProductModal')).show();
                },
                error: function () {
                    showToast('Failed to load product data', 'error');
                }
            });
        };

        // Delete product
        window.deleteProduct = function (id) {
            if (confirm('Are you sure you want to delete this product? This action cannot be undone.')) {
                $.ajax({
                    url: '/api/products/' + id,
                    method: 'DELETE',
                    xhrFields: {
                        withCredentials: true
                    },
                    success: function () {
                        showToast('Product deleted successfully!', 'success');
                        setTimeout(() => location.reload(), 1000);
                    },
                    error: function () {
                        showToast('Failed to delete product', 'error');
                    }
                });
            }
        };

        // Toast helper
        function showToast(message, type) {
            const toastClass = type === 'success' ? 'bg-success' : 'bg-danger';
            if ($('#toastContainer').length === 0) {
                $('body').append(`<div class="toast-container position-fixed top-0 end-0 p-3" id="toastContainer"></div>`);
            }

            const toast = $(`
                <div class="toast align-items-center text-white ${toastClass} border-0 mb-2" role="alert">
                    <div class="d-flex">
                        <div class="toast-body">${message}</div>
                        <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
                    </div>
                </div>
            `);

            $('#toastContainer').append(toast);
            const bsToast = new bootstrap.Toast(toast[0]);
            bsToast.show();

            toast.on('hidden.bs.toast', function () {
                $(this).remove();
            });
        }

        // Add product
        $('#addProductForm').on('submit', function (e) {
            e.preventDefault();

            const formData = {
                name: $('#name').val(),
                brand: $('#brand').val(),
                description: $('#description').val(),
                price: parseFloat($('#price').val()),
                stock: parseInt($('#stock').val()),
                category: $('#category').val(),
                switchType: $('#switchType').val() || null,
                layout: $('#layout').val() || null,
                imageUrl: $('#imageUrl').val(),
                featured: $('#featured').is(':checked')
            };

            $.ajax({
                url: '/api/products',
                method: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(formData),
                xhrFields: {
                    withCredentials: true
                },
                success: function () {
                    showToast('Product added successfully!', 'success');
                    bootstrap.Modal.getInstance(document.getElementById('addProductModal')).hide();
                    setTimeout(() => location.reload(), 1000);
                },
                error: function () {
                    showToast('Failed to add product', 'error');
                }
            });
        });

        // Edit product submit
        $('#editProductForm').on('submit', function (e) {
            e.preventDefault();

            const productId = $('#editProductId').val();
            const formData = {
                name: $('#editName').val(),
                brand: $('#editBrand').val(),
                description: $('#editDescription').val(),
                price: parseFloat($('#editPrice').val()),
                stock: parseInt($('#editStock').val()),
                category: $('#editCategory').val(),
                switchType: $('#editSwitchType').val() || null,
                layout: $('#editLayout').val() || null,
                imageUrl: $('#editImageUrl').val(),
                featured: $('#editFeatured').is(':checked')
            };

            $.ajax({
                url: '/api/products/' + productId,
                method: 'PUT',
                contentType: 'application/json',
                data: JSON.stringify(formData),
                xhrFields: {
                    withCredentials: true
                },
                success: function () {
                    showToast('Product updated successfully!', 'success');
                    bootstrap.Modal.getInstance(document.getElementById('editProductModal')).hide();
                    setTimeout(() => location.reload(), 1000);
                },
                error: function () {
                    showToast('Failed to update product', 'error');
                }
            });
        });
    });
</script>

</body>
</html>

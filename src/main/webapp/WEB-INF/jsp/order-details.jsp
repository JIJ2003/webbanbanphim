<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order #${order.id} - KeyCraft</title>
    <link href="/webjars/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>
    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container">
            <a class="navbar-brand" href="/">
                <i class="fas fa-keyboard"></i> KeyCraft
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="/">Home</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/products">Products</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/cart">Cart</a>
                    </li>
                </ul>
                <ul class="navbar-nav">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
                            <i class="fas fa-user"></i> ${currentUser.firstName} ${currentUser.lastName}
                        </a>
                        <ul class="dropdown-menu">
                            <li><a class="dropdown-item" href="/orders">My Orders</a></li>
                            <li><a class="dropdown-item" href="/auth/logout">Logout</a></li>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Order Details Content -->
    <div class="container my-4">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2><i class="fas fa-receipt"></i> Order #${order.id}</h2>
            <a href="/orders" class="btn btn-outline-secondary">
                <i class="fas fa-arrow-left"></i> Back to Orders
            </a>
        </div>
        
        <div class="row">
            <div class="col-lg-8">
                <!-- Order Information -->
                <div class="card mb-4">
                    <div class="card-header">
                        <h5><i class="fas fa-info-circle"></i> Order Information</h5>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-6">
                                <strong>Order Date:</strong><br>
<fmt:formatDate value="${createdAtDate}" pattern="MMM dd, yyyy 'at' HH:mm" />
                            </div>
                            <div class="col-md-6">
    <strong>Status:</strong><br>
    <c:choose>
        <c:when test="${order.status == 'PENDING'}">
            <span class="badge bg-warning">Pending</span>
        </c:when>
        <c:when test="${order.status == 'CONFIRMED'}">
            <span class="badge bg-success">Confirmed</span>
        </c:when>
        <c:when test="${order.status == 'SHIPPED'}">
            <span class="badge bg-info">Shipped</span>
            <c:if test="${not empty order.trackingCode}">
                <div class="mt-1 text-muted">Tracking: <code>${order.trackingCode}</code></div>
            </c:if>
        </c:when>
        <c:when test="${order.status == 'DELIVERED'}">
            <span class="badge bg-primary">Delivered</span>
            <c:if test="${not empty order.trackingCode}">
                <div class="mt-1 text-muted">Tracking: <code>${order.trackingCode}</code></div>
            </c:if>
        </c:when>
        <c:when test="${order.status == 'CANCELLED'}">
            <span class="badge bg-danger">Cancelled</span>
            <c:if test="${not empty order.trackingCode}">
                <div class="mt-1 text-muted">RETURNED: <code>${order.trackingCode}</code></div>
            </c:if>
        </c:when>
    </c:choose>
</div>

                        </div>
                    </div>
                </div>

                <!-- Order Items -->
                <div class="card mb-4">
                    <div class="card-header">
                        <h5><i class="fas fa-box"></i> Order Items</h5>
                    </div>
                    <div class="card-body">
                        <c:forEach items="${order.orderItems}" var="item">
                            <div class="row align-items-center mb-3 pb-3 border-bottom">
                                <div class="col-md-2">
                                    <img src="${item.product.imageUrl}" alt="${item.product.name}" 
                                         class="img-fluid rounded" style="height: 80px; object-fit: cover;">
                                </div>
                                <div class="col-md-5">
                                    <h6 class="mb-1">${item.product.name}</h6>
                                    <small class="text-muted">${item.product.brand}</small>
                                    <c:if test="${item.product.switchType != null}">
                                        <small class="text-muted d-block">${item.product.switchType} switches</small>
                                    </c:if>
                                </div>
                                <div class="col-md-2 text-center">
                                    <strong>Qty: ${item.quantity}</strong>
                                </div>
                                <div class="col-md-2 text-center">
                                    <strong>$${item.unitPrice}</strong>
                                </div>
                                <div class="col-md-1 text-end">
                                    <strong>$${item.totalPrice}</strong>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>

                <!-- Shipping & Billing Information -->
                <div class="row">
                    <div class="col-md-6">
                        <div class="card">
                            <div class="card-header">
                                <h6><i class="fas fa-truck"></i> Shipping Address</h6>
                            </div>
                            <div class="card-body">
                                <address>${order.shippingAddress}</address>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="card">
                            <div class="card-header">
                                <h6><i class="fas fa-file-invoice"></i> Billing Address</h6>
                            </div>
                            <div class="card-body">
                                <address>${order.billingAddress}</address>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            
            <!-- Order Summary -->
            <div class="col-lg-4">
                <div class="card">
                    <div class="card-header">
                        <h5><i class="fas fa-calculator"></i> Order Summary</h5>
                    </div>
                    <div class="card-body">
                        <c:set var="subtotal" value="0" />
                        <c:forEach items="${order.orderItems}" var="item">
                            <c:set var="subtotal" value="${subtotal + item.totalPrice}" />
                        </c:forEach>
                        
                        <div class="d-flex justify-content-between mb-2">
                            <span>Subtotal:</span>
                            <span>$${subtotal}</span>
                        </div>
                        <div class="d-flex justify-content-between mb-2">
                            <span>Shipping:</span>
                            <span class="text-success">FREE</span>
                        </div>
                        <div class="d-flex justify-content-between mb-2">
                            <span>Tax:</span>
                            <span>$0.00</span>
                        </div>
                        <hr>
                        <div class="d-flex justify-content-between">
                            <strong>Total:</strong>
                            <strong>$${order.totalAmount}</strong>
                        </div>
                    </div>
                </div>

                <div class="card mt-3">
                    <div class="card-header">
                        <h6><i class="fas fa-credit-card"></i> Payment Method</h6>
                    </div>
                    <div class="card-body">
                        <p>${order.paymentMethod}</p>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="/webjars/jquery/jquery.min.js"></script>
    <script src="/webjars/bootstrap/js/bootstrap.bundle.min.js"></script>
</body>
</html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - KeyCraft</title>
    <link href="/webjars/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
        }
        .login-card {
            background: rgba(255, 255, 255, 0.95);
            border-radius: 15px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-6 col-lg-4">
                <div class="login-card p-4">
                    <div class="text-center mb-4">
                        <h1 class="h3">
                            <i class="fas fa-keyboard text-primary"></i> KeyCraft
                        </h1>
                        <p class="text-muted">Sign in to your account</p>
                    </div>

                    <!-- Error Messages -->
                    <c:if test="${error != null}">
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            <i class="fas fa-exclamation-circle"></i> ${error}
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                    </c:if>

                    <!-- Login Form -->
<form action="/auth/login" method="post">
    <div class="mb-3">
        <label for="username" class="form-label">Email Address</label>
        <div class="input-group">
            <span class="input-group-text">
                <i class="fas fa-envelope"></i>
            </span>
            <input type="text" class="form-control" id="username" name="username" required>
        </div>
    </div>

    <div class="mb-3">
        <label for="password" class="form-label">Password</label>
        <div class="input-group">
            <span class="input-group-text">
                <i class="fas fa-lock"></i>
            </span>
            <input type="password" class="form-control" id="password" name="password" required>
        </div>
    </div>


                        <button type="submit" class="btn btn-primary w-100 mb-3">
                            <i class="fas fa-sign-in-alt"></i> Sign In
                        </button>
                    </form>

                    <!-- Test Accounts Info -->
                    <div class="alert alert-info">
                        <strong>Test Accounts:</strong><br>
                        <small>
                            Admin: admin@keycraft.com / admin123<br>
                            Customer: customer@keycraft.com / customer123
                        </small>
                    </div>

                    <div class="text-center">
                        <p class="mb-0">Don't have an account? <a href="/signup" class="text-decoration-none">Sign up here</a></p>
                        <p class="mt-2"><a href="/" class="text-muted text-decoration-none">← Back to Home</a></p>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="/webjars/jquery/jquery.min.js"></script>
    <script src="/webjars/bootstrap/js/bootstrap.bundle.min.js"></script>
</body>
</html>
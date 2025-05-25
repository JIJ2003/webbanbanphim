import { useState } from "react";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { useAuth } from "@/hooks/useAuth";
import { useCart } from "@/hooks/useCart";
import { Link } from "wouter";
import { ShoppingCart, User, Search, Menu, LogOut } from "lucide-react";
import { Sheet, SheetContent, SheetTrigger } from "@/components/ui/sheet";
import { Input } from "@/components/ui/input";

export default function Header() {
  const { user, isAuthenticated } = useAuth();
  const { itemCount } = useCart();
  const [isSearchOpen, setIsSearchOpen] = useState(false);

  return (
    <header className="bg-white shadow-sm border-b border-gray-200 sticky top-0 z-50">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-16">
          {/* Logo */}
          <div className="flex items-center">
            <Link href="/">
              <h1 className="text-2xl font-bold text-primary cursor-pointer">KeyCraft</h1>
            </Link>
          </div>

          {/* Desktop Navigation */}
          <nav className="hidden md:flex space-x-8">
            <a href="#" className="text-gray-700 hover:text-primary font-medium transition-colors">
              Keyboards
            </a>
            <a href="#" className="text-gray-700 hover:text-primary font-medium transition-colors">
              Accessories
            </a>
            <a href="#" className="text-gray-700 hover:text-primary font-medium transition-colors">
              Custom Builds
            </a>
            <a href="#" className="text-gray-700 hover:text-primary font-medium transition-colors">
              Services
            </a>
            <a href="#" className="text-gray-700 hover:text-primary font-medium transition-colors">
              About
            </a>
          </nav>

          {/* Right side actions */}
          <div className="flex items-center space-x-4">
            {/* Search */}
            <div className="hidden sm:block">
              {isSearchOpen ? (
                <div className="flex items-center space-x-2">
                  <Input 
                    placeholder="Search products..." 
                    className="w-48"
                    onBlur={() => setIsSearchOpen(false)}
                    autoFocus
                  />
                </div>
              ) : (
                <Button
                  variant="ghost"
                  size="sm"
                  onClick={() => setIsSearchOpen(true)}
                >
                  <Search className="h-5 w-5" />
                </Button>
              )}
            </div>

            {/* User Menu */}
            {isAuthenticated ? (
              <div className="flex items-center space-x-2">
                <span className="hidden sm:block text-sm text-gray-600">
                  {user?.firstName || user?.email}
                </span>
                <Link href="/admin">
                  <Button variant="ghost" size="sm">
                    <User className="h-5 w-5" />
                  </Button>
                </Link>
                <Button variant="ghost" size="sm" asChild>
                  <a href="/api/logout">
                    <LogOut className="h-5 w-5" />
                  </a>
                </Button>
              </div>
            ) : (
              <Button variant="ghost" size="sm" asChild>
                <a href="/api/login">
                  <User className="h-5 w-5" />
                </a>
              </Button>
            )}

            {/* Cart */}
            <Button variant="ghost" size="sm" className="relative">
              <ShoppingCart className="h-5 w-5" />
              {itemCount > 0 && (
                <Badge className="absolute -top-2 -right-2 bg-accent text-white text-xs h-5 w-5 flex items-center justify-center rounded-full">
                  {itemCount}
                </Badge>
              )}
            </Button>

            {/* Mobile menu */}
            <Sheet>
              <SheetTrigger asChild>
                <Button variant="ghost" size="sm" className="md:hidden">
                  <Menu className="h-5 w-5" />
                </Button>
              </SheetTrigger>
              <SheetContent>
                <div className="flex flex-col space-y-4 mt-8">
                  <a href="#" className="text-gray-700 hover:text-primary font-medium transition-colors">
                    Keyboards
                  </a>
                  <a href="#" className="text-gray-700 hover:text-primary font-medium transition-colors">
                    Accessories
                  </a>
                  <a href="#" className="text-gray-700 hover:text-primary font-medium transition-colors">
                    Custom Builds
                  </a>
                  <a href="#" className="text-gray-700 hover:text-primary font-medium transition-colors">
                    Services
                  </a>
                  <a href="#" className="text-gray-700 hover:text-primary font-medium transition-colors">
                    About
                  </a>
                </div>
              </SheetContent>
            </Sheet>
          </div>
        </div>
      </div>
    </header>
  );
}

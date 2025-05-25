import { useState } from "react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Checkbox } from "@/components/ui/checkbox";
import { Label } from "@/components/ui/label";
import { Slider } from "@/components/ui/slider";

interface FilterSidebarProps {
  onFiltersChange: (filters: Record<string, any>) => void;
}

export default function FilterSidebar({ onFiltersChange }: FilterSidebarProps) {
  const [selectedCategories, setSelectedCategories] = useState<string[]>([]);
  const [selectedBrands, setSelectedBrands] = useState<string[]>([]);
  const [selectedSwitchTypes, setSelectedSwitchTypes] = useState<string[]>([]);
  const [priceRange, setPriceRange] = useState([0, 500]);

  const categories = [
    "mechanical-keyboards",
    "keycaps", 
    "switches",
    "accessories"
  ];

  const brands = [
    "Keychron",
    "Ducky", 
    "Leopold",
    "Varmilo",
    "HHKB"
  ];

  const switchTypes = [
    "linear",
    "tactile",
    "clicky"
  ];

  const handleCategoryChange = (category: string, checked: boolean) => {
    const newCategories = checked 
      ? [...selectedCategories, category]
      : selectedCategories.filter(c => c !== category);
    
    setSelectedCategories(newCategories);
    updateFilters({ category: newCategories.length > 0 ? newCategories[0] : undefined });
  };

  const handleBrandChange = (brand: string, checked: boolean) => {
    const newBrands = checked 
      ? [...selectedBrands, brand]
      : selectedBrands.filter(b => b !== brand);
    
    setSelectedBrands(newBrands);
    updateFilters({ brand: newBrands.length > 0 ? newBrands[0] : undefined });
  };

  const handleSwitchTypeChange = (switchType: string, checked: boolean) => {
    const newSwitchTypes = checked 
      ? [...selectedSwitchTypes, switchType]
      : selectedSwitchTypes.filter(s => s !== switchType);
    
    setSelectedSwitchTypes(newSwitchTypes);
    updateFilters({ switchType: newSwitchTypes.length > 0 ? newSwitchTypes[0] : undefined });
  };

  const handlePriceChange = (value: number[]) => {
    setPriceRange(value);
    updateFilters({ 
      minPrice: value[0] > 0 ? value[0] : undefined,
      maxPrice: value[1] < 500 ? value[1] : undefined
    });
  };

  const updateFilters = (newFilters: Record<string, any>) => {
    const filters = {
      category: selectedCategories.length > 0 ? selectedCategories[0] : undefined,
      brand: selectedBrands.length > 0 ? selectedBrands[0] : undefined,
      switchType: selectedSwitchTypes.length > 0 ? selectedSwitchTypes[0] : undefined,
      minPrice: priceRange[0] > 0 ? priceRange[0] : undefined,
      maxPrice: priceRange[1] < 500 ? priceRange[1] : undefined,
      ...newFilters
    };

    // Remove undefined values
    const cleanFilters = Object.fromEntries(
      Object.entries(filters).filter(([_, value]) => value !== undefined)
    );

    onFiltersChange(cleanFilters);
  };

  return (
    <Card>
      <CardHeader>
        <CardTitle>Filters</CardTitle>
      </CardHeader>
      <CardContent className="space-y-6">
        {/* Price Range */}
        <div>
          <h5 className="font-medium mb-3">Price Range</h5>
          <div className="px-2">
            <Slider
              value={priceRange}
              onValueChange={handlePriceChange}
              max={500}
              step={10}
              className="mb-2"
            />
            <div className="flex justify-between text-sm text-gray-600">
              <span>${priceRange[0]}</span>
              <span>${priceRange[1]}</span>
            </div>
          </div>
        </div>

        {/* Categories */}
        <div>
          <h5 className="font-medium mb-3">Category</h5>
          <div className="space-y-2">
            {categories.map((category) => (
              <div key={category} className="flex items-center space-x-2">
                <Checkbox
                  id={category}
                  checked={selectedCategories.includes(category)}
                  onCheckedChange={(checked) => 
                    handleCategoryChange(category, checked as boolean)
                  }
                />
                <Label htmlFor={category} className="text-sm capitalize">
                  {category.replace('-', ' ')}
                </Label>
              </div>
            ))}
          </div>
        </div>

        {/* Brands */}
        <div>
          <h5 className="font-medium mb-3">Brand</h5>
          <div className="space-y-2">
            {brands.map((brand) => (
              <div key={brand} className="flex items-center space-x-2">
                <Checkbox
                  id={brand}
                  checked={selectedBrands.includes(brand)}
                  onCheckedChange={(checked) => 
                    handleBrandChange(brand, checked as boolean)
                  }
                />
                <Label htmlFor={brand} className="text-sm">
                  {brand}
                </Label>
              </div>
            ))}
          </div>
        </div>

        {/* Switch Types */}
        <div>
          <h5 className="font-medium mb-3">Switch Type</h5>
          <div className="space-y-2">
            {switchTypes.map((switchType) => (
              <div key={switchType} className="flex items-center space-x-2">
                <Checkbox
                  id={switchType}
                  checked={selectedSwitchTypes.includes(switchType)}
                  onCheckedChange={(checked) => 
                    handleSwitchTypeChange(switchType, checked as boolean)
                  }
                />
                <Label htmlFor={switchType} className="text-sm capitalize">
                  {switchType}
                </Label>
              </div>
            ))}
          </div>
        </div>
      </CardContent>
    </Card>
  );
}

export interface RestaurantTable {
  id: number;
  capacity: number;
  zone: string;
  x: number;
  y: number;
  isOccupied: boolean;
  isWindowSeat: boolean;
}

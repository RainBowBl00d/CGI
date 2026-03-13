export interface RestaurantTable {
  id: number;
  capacity: number;
  zone: string;
  x: number;
  y: number;
  windowSeat: boolean;
  quietArea: boolean;
  reservations: Reservation[];
}
export interface Reservation{
  id:number;
  groupSize: number;
  reservationTimeStart: string;
  reservationTimeEnd: string;
  tables: RestaurantTable[];
}

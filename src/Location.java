public record Location(int x,int y) {
    public Location{if (x>7|| x<0 || y >7 || y<0){
        throw new IllegalArgumentException("x and y coordinates must be between 0 and 7 inclusively");
    }
    }
}
//x designates the column number
//y designates the row number

package GISRecord;

public enum RecordLocations {
	FID(0),
	FName(1),
	FClass(2),
	StateAlphCode(3),
	StateNumCode(4),
	CountyName(5),
	CountyNumCode(6),
	PrimeLat(7),
	PrimeLong(8),
	PrimeLatDec(9),
	PrimeLongDec(10),
	SourceLat(11),
	SourceLong(12),
	SourceLatDec(13),
	SourceLongDec(14),
	ElevationM(15),
	ElevationF(16),
	MapName(17),
	Created(18),
	Edited(19);
	
	private int value;
    private RecordLocations(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

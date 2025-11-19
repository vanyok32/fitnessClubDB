package fitness.club.mapper;

public interface BaseMapper<Request, Response, E>{
    public E toEntity(Request request);
    public Response toResponseDto(E response);
}

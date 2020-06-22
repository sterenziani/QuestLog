package ar.edu.itba.paw.model.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "images")
public class Image
{
	@Id
	@Column(name = "image")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "images_image_seq")
	@SequenceGenerator(allocationSize = 1, sequenceName = "images_image_seq", name = "images_image_seq")
    private Long id;
	
	@Column(name = "image_name", length = 100, nullable = false, unique = true)
    private String imageName;
	
	@Column(name = "image_data")
    private byte[] imageData;

    public Image()
    {
    	
    }
    
    @Deprecated
    public Image(long id, String imageName, byte[] imageData){
		this.id         = id;
        this.imageName  = imageName;
        this.imageData  = imageData;
    }
    
    public Image(String imageName, byte[] imageData)
    {
		this.imageName  = imageName;
        this.imageData  = imageData;
    }

    public long getId() {
        return id;
    }

    public String   getImageName() {
        return imageName;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }
}

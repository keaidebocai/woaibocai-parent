package top.woaibocai.model.Do;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: woaibocai-parent
 * @description: 泛型的kv
 * @author: woaibocai
 * @create: 2024-01-21 15:25
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "kv: T")
public class KeyValue<K,V> {
    @Schema(description = "k")
    private K k;
    @Schema(description = "v")
    private V v;
}

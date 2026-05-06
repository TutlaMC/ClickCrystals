[<-- Back to Legend](../legend.md)

# Command Name: Toggle Input
Keyword: toggle_input

### Usages
```
toggle_input attack false
toggle_input attack true
toggle_input backward false
toggle_input backward true
toggle_input cancel
toggle_input container false
toggle_input container true
toggle_input forward false
toggle_input forward true
toggle_input inventory false
toggle_input inventory true
toggle_input jump false
toggle_input jump true
toggle_input key ... false
toggle_input key ... true
toggle_input left false
toggle_input left true
toggle_input lock_cursor false
toggle_input lock_cursor true
toggle_input middle false
toggle_input middle true
toggle_input mouse_wheel_down false
toggle_input mouse_wheel_down true
toggle_input mouse_wheel_up false
toggle_input mouse_wheel_up true
toggle_input right false
toggle_input right true
toggle_input sneak false
toggle_input sneak true
toggle_input sprint false
toggle_input sprint true
toggle_input strafe_left false
toggle_input strafe_left true
toggle_input strafe_right false
toggle_input strafe_right true
toggle_input unlock_cursor false
toggle_input unlock_cursor true
toggle_input use false
toggle_input use true
```

### Regex
```regexp
(((toggle_input)( (attack|use|forward|backward|strafe_left|strafe_right|jump|sprint|sneak|lock_cursor|unlock_cursor|left|right|middle|container|inventory|mouse_wheel_up|mouse_wheel_down))( (true|false)))|((toggle_input)( (key))( (\S+))( (true|false)))|((toggle_input)( (cancel))))
```

### Raw Documentation
```yml
# toggle_input <input> (true|false)
# toggle_input key ... (true|false)
# toggle_input cancel
```

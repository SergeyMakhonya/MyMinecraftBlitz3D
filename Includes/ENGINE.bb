;=========================
;       Сохранение игры
;=========================
Function SaveGame()
   file = WriteFile("SAVE\save.sav") ;Открываем файл для записи
   WriteLine file,kol_blocks ;Записываем количество блоков
   For b.Blocks = Each Blocks
      WriteLine file,xEntityX(b\ent) ;Записываем X координату блока
      WriteLine file,xEntityY(b\ent) ;Записываем Y координату блока
      WriteLine file,xEntityZ(b\ent) ;Записываем Z координату блока
      WriteLine file,b\typ$ ;Записываем тип блока
   Next
   CloseFile file ;Закрываем файл
End Function

;=========================
;          Загрузка игры
;=========================
Function LoadGame()
   DeleteAllBlocks() ;Удаляем все блоки в игре
   
   kol_blocks=0
   
   file = ReadFile("SAVE\save.sav") ;Открываем файл для чтения
   kol = ReadLine(file) ;Считываем количество блоков
   For i=1 To kol
      x#=ReadLine(file) ;Считываем X координату блока
      y#=ReadLine(file) ;Считываем Y координату блока
      z#=ReadLine(file) ;Считываем Z координату блока
      typ$=ReadLine(file) ;Считываем тип блока
      CreateBlock(x#,y#,z#,typ$)
   Next
   
   CloseFile file ;Закрываем файл
End Function

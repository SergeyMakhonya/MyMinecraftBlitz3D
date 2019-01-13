;Подключаем Xors3D
Include "Xors3D.bb"

;Подключаем наш инклуд с функциями
Include "includes/ENGINE.bb"

Type Blocks
   Field ent ;Объект
   Field typ$ ;Тип блока
End Type

Global kol_blocks ;Количество блоков
Global sel_block ;Выделенный блок

;xGraphics3D ширина, высота, глубина цвета, False-Оконный режим True-Полный экран, False-Максимум FPS если True-60 FPS(Кадров)
xGraphics3D 1024,768,32,False,True
xSetBuffer xBackBuffer()

xAppTitle "Мой Minecraft - DirectX 9" ;Загололовок

xAntiAlias True ;Включить Сглаживание

;Загружаем шрифт Arial с размером 12
Global fnt12=xLoadFont("Arial",12)
xSetFont fnt12

Global camera_piv=xCreatePivot()
Global camera=xCreateCamera()
xPositionEntity camera,0,5,-15
xCameraClsColor camera,0,165,250
;xCameraRange camera,.05,50
Global light=xCreateLight()
xRotateEntity light,30,0,0

;===========

Global ACube=xCreatePivot()
;Верх
Global ACube_top=xCreateCube(ACube)
xPositionEntity ACube_top,0,.5,0
xScaleEntity ACube_top,.48,.05,.48
xEntityPickMode ACube_top,2
xNameEntity ACube_top,"top"
;xEntityAlpha ACube_top,0
;Низ
Global ACube_bottom=xCreateCube(ACube)
xPositionEntity ACube_bottom,0,-.5,0
xScaleEntity ACube_bottom,.48,.05,.48
xEntityPickMode ACube_bottom,2
xNameEntity ACube_bottom,"bottom"
;xEntityAlpha ACube_bottom,0
;Перед
Global ACube_front=xCreateCube(ACube)
xPositionEntity ACube_front,0,0,.5
xScaleEntity ACube_front,.48,.48,.05
xEntityPickMode ACube_front,2
xNameEntity ACube_front,"front"
;xEntityAlpha ACube_front,0
;Зад
Global ACube_back=xCreateCube(ACube)
xPositionEntity ACube_back,0,0,-.5
xScaleEntity ACube_back,.48,.48,.05
xEntityPickMode ACube_back,2
xNameEntity ACube_back,"back"
;xEntityAlpha ACube_back,0
;Левый бок
Global ACube_left=xCreateCube(ACube)
xPositionEntity ACube_left,-.5,0,0
xScaleEntity ACube_left,.05,.48,.48
xEntityPickMode ACube_left,2
xNameEntity ACube_left,"left"
;xEntityAlpha ACube_left,0
;Правый бок
Global ACube_right=xCreateCube(ACube)
xPositionEntity ACube_right,.5,0,0
xScaleEntity ACube_right,.05,.48,.48
xEntityPickMode ACube_right,2
xNameEntity ACube_right,"right"
;xEntityAlpha ACube_right,0

;Скрываем вспомогательный объект ACube
xHideEntity ACube

;===========

Global tex_ground=xLoadTexture("Textures\ground_down.jpg")

Global pick ;Луч исходящий из камеры(короче щупальце)

Global pick_ent_0 ;Предыдущий объект

;For x=-10 To 10
;For z=-10 To 10
CreateBlock(x,0,z,"ground")
;Next
;Next

; ++++++++++++++++++++++
; Начало игрового цикла
; ++++++++++++++++++++++

While Not xKeyDown(1)
   
   ;Движение игрока вперёд, назад, влево, вправо
   If xKeyDown(200) Or xKeyDown(17) Then xMoveEntity camera_piv,0,0,.1
   If xKeyDown(208) Or xKeyDown(31) Then xMoveEntity camera_piv,0,0,-.1
   If xKeyDown(203) Or xKeyDown(30) Then xMoveEntity camera_piv,-.1,0,0
   If xKeyDown(205) Or xKeyDown(32) Then xMoveEntity camera_piv,.1,0,0
   
   ;Движение игрока вверх, вниз
   If xKeyDown(57) Then xMoveEntity camera_piv,0,.1,0
   If xKeyDown(29) Then xMoveEntity camera_piv,0,-.1,0
   
   If xKeyHit(64) Then LoadGame() ;Загружаем сохранение при нажатии клавиши F6
   If xKeyHit(63) Then SaveGame() ;Созраняем игру при нажатии клавиши F5
   
   xPositionEntity camera,xEntityX(camera_piv),xEntityY(camera_piv)+2.5,xEntityZ(camera_piv)
   
   ;Мышка
   ;HidePointer
   MXS#=xMouseXSpeed()*0.1:MYS#=xMouseYSpeed()*0.1
   
   xTurnEntity camera,MYS#,0,0
   xTurnEntity camera_piv,0,-MXS#,0
   
   If xEntityPitch(camera)>90 xRotateEntity camera,90,xEntityYaw(camera_piv),0
   If xEntityPitch(camera)<-90 xRotateEntity camera,-90,xEntityYaw(camera_piv),0
   
   xRotateEntity camera,xEntityPitch(camera),xEntityYaw(camera_piv),0
   xMoveMouse xGraphicsWidth()/2,xGraphicsHeight()/2
   
   If pick_ent_0<>0 Then xEntityColor pick_ent_0,255,255,255 ;Окрашиваем предыдущий объект в белый цвет
   
   pick=xCameraPick(camera,xMouseX(),xMouseY())
   If pick<>0 Then dist=xEntityDistance(camera,pick)
   
   If pick<>0 And pick<>ACube_top And pick<>ACube_bottom And pick<>ACube_front And pick<>ACube_back And pick<>ACube_left And pick<>ACube_right Then
      sel_block=pick
      pick_ent_0=sel_block
   End If
   
   If pick<>0 And pick<>ACube_top And pick<>ACube_bottom And pick<>ACube_front And pick<>ACube_back And pick<>ACube_left And pick<>ACube_right Then xPositionEntity ACube,Int(xEntityX(pick)),Int(xEntityY(pick)),Int(xEntityZ(pick)):xShowEntity ACube
   
   ;Окрашиваем только тот объект который ближе или равен 5 шагам к камере
   If dist<=5 And sel_block<>0 Then
      xEntityColor sel_block,255,255,0 ;Окрашиваем текущий объект в жёлтый цвет
   End If
   
   ;========================
   
   If dist<=5 Then
      If xMouseHit(1) And sel_block<>0 And pick<>0 Then DeleteBlock(sel_block):sel_block=0:xHideEntity ACube
      If xMouseHit(2) And pick<>0 Then CreateBlockOnPick(xPickedX#(),xPickedY#(),xPickedZ#(),"ground")
   End If
   
   ;========================
   
   xUpdateWorld
   xRenderWorld
   
   xText 5,5,"FPS: "+xGetFPS()
   xText 5,20,"Polygons: "+xTrisRendered()/2
   
   xText xGraphicsWidth()/2,50,"KOL-VO BLOCKS="+kol_blocks
   
   ;xText xMouseX(),xMouseY()-55,"ID: "+pick,True,True
  ; If pick<>0 Then 
      ;xText xMouseX(),xMouseY()-35,"TYPE: "+xEntityName$(sel_block),True,True
      ;xText xMouseX(),xMouseY()-15,"DISTANCE: "+dist,True,True
 ;  End If
   
   xFlip
Wend
; ++++++++++++++++++++++

End

;Функция создания нового блока
Function CreateBlock(x#,y#,z#,typ$)
   b.Blocks = New Blocks
   
   Select typ$
   Case "ground"  
      b\ent=xCreateCube()
      xEntityTexture b\ent,tex_ground
   End Select
   
   kol_blocks=kol_blocks+1
   b\typ$=typ$
   
   xPositionEntity b\ent,x#,y#,z#
   xScaleEntity b\ent,.5,.5,.5
   xEntityPickMode b\ent,2 ;Делаем объект видимым лучу выходящим из камеры
   xNameEntity b\ent,typ$ ;Присваеваем тип объекта в его имя
End Function

;Функция удаления блока
Function DeleteBlock(ent)
For b.Blocks = Each Blocks
   If b\ent=ent Then
      xFreeEntity b\ent
      Delete b
      kol_blocks=kol_blocks-1
      
      Goto end_delete_block
   End If
Next
.end_delete_block
End Function

;Функция удаления всех блоков
Function DeleteAllBlocks()
kol_blocks=0
For b.Blocks = Each Blocks
   xFreeEntity b\ent
   Delete b
Next

sel_block=0
pick=0
pick_ent_0=0
End Function

Function CreateBlockOnPick(x#,y#,z#,typ$)
   piv=xCreatePivot()
   
   Select xEntityName(pick)
      Case "top": xPositionEntity piv,Int(xEntityX(ACube)),Int(xEntityY(ACube))+1,Int(xEntityZ(ACube))
      Case "bottom": xPositionEntity piv,Int(xEntityX(ACube)),Int(xEntityY(ACube))-1,Int(xEntityZ(ACube))
      Case "front": xPositionEntity piv,Int(xEntityX(ACube)),Int(xEntityY(ACube)),Int(xEntityZ(ACube))+1
      Case "back": xPositionEntity piv,Int(xEntityX(ACube)),Int(xEntityY(ACube)),Int(xEntityZ(ACube))-1
      Case "left": xPositionEntity piv,Int(xEntityX(ACube))-1,Int(xEntityY(ACube)),Int(xEntityZ(ACube))
      Case "right": xPositionEntity piv,Int(xEntityX(ACube))+1,Int(xEntityY(ACube)),Int(xEntityZ(ACube))
   End Select
   
   CreateBlock(xEntityX(piv),xEntityY(piv),xEntityZ(piv),typ$)
   xFreeEntity piv
End Function
;=========================
;       ���������� ����
;=========================
Function SaveGame()
   file = WriteFile("SAVE\save.sav") ;��������� ���� ��� ������
   WriteLine file,kol_blocks ;���������� ���������� ������
   For b.Blocks = Each Blocks
      WriteLine file,xEntityX(b\ent) ;���������� X ���������� �����
      WriteLine file,xEntityY(b\ent) ;���������� Y ���������� �����
      WriteLine file,xEntityZ(b\ent) ;���������� Z ���������� �����
      WriteLine file,b\typ$ ;���������� ��� �����
   Next
   CloseFile file ;��������� ����
End Function

;=========================
;          �������� ����
;=========================
Function LoadGame()
   DeleteAllBlocks() ;������� ��� ����� � ����
   
   kol_blocks=0
   
   file = ReadFile("SAVE\save.sav") ;��������� ���� ��� ������
   kol = ReadLine(file) ;��������� ���������� ������
   For i=1 To kol
      x#=ReadLine(file) ;��������� X ���������� �����
      y#=ReadLine(file) ;��������� Y ���������� �����
      z#=ReadLine(file) ;��������� Z ���������� �����
      typ$=ReadLine(file) ;��������� ��� �����
      CreateBlock(x#,y#,z#,typ$)
   Next
   
   CloseFile file ;��������� ����
End Function
